package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class Output extends OutputStream{
	private final JTextArea output = new JTextArea();
	public final InputStream input = new Input(this.output);
	
	public Output(){
		this.output.setEditable(true);
	}
	
	@Override
	public void write(int b) throws IOException{
		this.output.append(String.valueOf((char)b));
		this.output.setCaretPosition(this.output.getDocument().getLength());
	}
	
	public JTextArea getTextArea(){
		return this.output;
	}
}
class Input extends InputStream{
	byte in = -1;
	
	public Input(final JTextArea text) {
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				Input.this.in = (byte) e.getKeyChar();
				super.keyTyped(e);
			}
		});
	}

	@Override
	public int read() throws IOException{
		while(true){
			try {
				Thread.sleep(10);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			if(this.in == '\n'){
				this.in = -2;
				return '\n';
			} else if(this.in == -2){
				this.in = -1;
				return -1;
			} else if(this.in != -1){
				byte temp = this.in;
				this.in = -1;
				return temp;
			} else {
				continue;
			}
		}
	}
}

