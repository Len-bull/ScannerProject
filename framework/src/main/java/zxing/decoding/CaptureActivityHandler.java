/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package zxing.decoding;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;

import java.util.Vector;

import zxing.camera.CameraManager;
import zxing.cons.ZxingCons;
import zxing.view.CaptureView;

/**
 * This class handles all the messaging which comprises the state machine for capture.
 */
public final class CaptureActivityHandler extends Handler {

  private static final String TAG = CaptureActivityHandler.class.getSimpleName();

  private final CaptureView captureView;
  private final DecodeThread decodeThread;
  private State state;

  private enum State {
      PREVIEW,
      SUCCESS,
      DONE
  }

  /**
   * @param resultPointCallback 可能的二维码的点位置回调
   * */
  public CaptureActivityHandler(CaptureView captureView,
                                @Nullable ResultPointCallback resultPointCallback,
                                Vector<BarcodeFormat> decodeFormats,
                                String characterSet) {
      this.captureView = captureView;
      decodeThread = new DecodeThread(captureView, decodeFormats, characterSet,/*new ViewfinderResultPointCallback(captureView.getViewfinderView())*/resultPointCallback);
      decodeThread.start();
      state = State.SUCCESS;
      // Start ourselves capturing previews and decoding.
      CameraManager.get().startPreview();
      restartPreviewAndDecode();
  }

  @Override
  public void handleMessage(Message message) {
    if(message.what == ZxingCons.AUTO_FOCUS){  /*自动对焦*/
        //Log.d(TAG, "Got auto-focus message");
        // When one auto focus pass finishes, start another. This is the closest thing to
        // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
        if (state == State.PREVIEW) {
            CameraManager.get().requestAutoFocus(this, ZxingCons.AUTO_FOCUS);
        }
    }else if(message.what == ZxingCons.RESTART_PREVIEW){ /*开始扫描并解码*/
        Log.d(TAG, "Got restart preview message");
        restartPreviewAndDecode();
    }else if(message.what == ZxingCons.DECODE_SUCCEEDED){    /*解码成功*/
        Log.d(TAG, "Got decode succeeded message");
        state = State.SUCCESS;
        Bundle bundle = message.getData();
        Bitmap barcode = bundle == null ? null:(Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
        captureView.handleDecode((Result) message.obj, barcode);
    }else if(message.what == ZxingCons.DECODE_FAILED){   /*解码失败*/
        // We're decoding as fast as possible, so when one decode fails, start another.
        state = State.PREVIEW;
        CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), ZxingCons.DECODE);
    }
  }

  public void quitSynchronously() {
    state = State.DONE;
    CameraManager.get().stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(), ZxingCons.QUIT);
    quit.sendToTarget();
    try {
      decodeThread.join();
    } catch (InterruptedException e) {
      // continue
    }

    // Be absolutely sure we don't send any queued up messages
    removeMessages(ZxingCons.DECODE_SUCCEEDED);
    removeMessages(ZxingCons.DECODE_FAILED);
  }

  public void restartPreviewAndDecode() {
      if (state == State.SUCCESS) {
          state = State.PREVIEW;
          CameraManager.get().requestPreviewFrame(decodeThread.getHandler(), ZxingCons.DECODE);
          CameraManager.get().requestAutoFocus(this, ZxingCons.AUTO_FOCUS);
          captureView.drawViewfinder();
      }
  }

}
