package com.example.testrecord;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	 /** Called when the activity is first created. */    
    Button btnRecord, btnStop, btnExit;    
    SeekBar skbVolume;//��������    
    boolean isRecording = false;//�Ƿ�¼�ŵı��    
    static final int frequency = 8000;//44100;    
    static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;    
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;    
    int recBufSize,playBufSize;    
    AudioRecord audioRecord;    
    AudioTrack audioTrack;    
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		    setTitle("������");    
	        recBufSize = AudioRecord.getMinBufferSize(frequency,    
	                channelConfiguration, audioEncoding);    
	    
	        playBufSize=AudioTrack.getMinBufferSize(frequency,    
	                channelConfiguration, audioEncoding);    
	        // -----------------------------------------    
	        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,    
	                channelConfiguration, audioEncoding, recBufSize*10);    
	    
	        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,    
	                channelConfiguration, audioEncoding,    
	                playBufSize, AudioTrack.MODE_STREAM);    
	        //------------------------------------------    
	        btnRecord = (Button) this.findViewById(R.id.btnRecord);    
	        btnRecord.setOnClickListener(new ClickEvent());    
	        btnStop = (Button) this.findViewById(R.id.btnStop);    
	        btnStop.setOnClickListener(new ClickEvent());    
	        btnExit = (Button) this.findViewById(R.id.btnExit);    
	        btnExit.setOnClickListener(new ClickEvent());    
	        skbVolume=(SeekBar)this.findViewById(R.id.skbVolume);    
	        skbVolume.setMax(100);//�������ڵļ���    
	        skbVolume.setProgress(100);//����seekbar��λ��ֵ    
	      
	        audioTrack.setStereoVolume(1.0f, 1.0f);//���õ�ǰ������С  
	        
	        
	        skbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {    
                
	            @Override    
	            public void onStopTrackingTouch(SeekBar seekBar) {    
	                float vol=(float)(seekBar.getProgress())/(float)(seekBar.getMax());    
	                audioTrack.setStereoVolume(vol, vol);//��������    
	            }    
	                
	            @Override    
	            public void onStartTrackingTouch(SeekBar seekBar) {    
	                // TODO Auto-generated method stub    
	            }    
	                
	            @Override    
	            public void onProgressChanged(SeekBar seekBar, int progress,    
	                    boolean fromUser) {    
	                // TODO Auto-generated method stub    
	            }    
	        });    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 class ClickEvent implements View.OnClickListener {    
		    
	        @Override    
	        public void onClick(View v) {    
	            if (v == btnRecord) {    
	                isRecording = true;    
	                new RecordPlayThread().start();// ��һ���̱߳�¼�߷�    
	            } else if (v == btnStop) {    
	                isRecording = false;    
	            } else if (v == btnExit) {    
	                isRecording = false;    
	                MainActivity.this.finish();    
	            }    
	        }    
	    }    
	    
	    class RecordPlayThread extends Thread {    
	        public void run() {    
	            try {    
	  
	                byte[] buffer = new byte[recBufSize];    
	                
	                 audioRecord.startRecording();//��ʼ¼��    
					
	                audioTrack.play();//��ʼ����    
	                    
	                while (isRecording) {    
	                    //��MIC�������ݵ�������    
	                    int bufferReadResult = audioRecord.read(buffer, 0,    
	                            recBufSize);    
	    
	                    byte[] tmpBuf = new byte[bufferReadResult];    
	                    System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);    
	                    //д�����ݼ�����    
	                    audioTrack.write(tmpBuf, 0, tmpBuf.length);    
	                }    
	                audioTrack.stop();    
	                audioRecord.stop();    
	            } catch (Exception e) {
	            	e.printStackTrace();
	               System.out.println("ffff error");
  
	            }    
	        }    
	        
	        
	    }; 
	
	
}
