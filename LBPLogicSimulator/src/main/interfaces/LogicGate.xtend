package main.interfaces

import java.awt.Image
import java.awt.Rectangle
import java.awt.Toolkit
import java.util.ArrayList
import java.util.List
import main.Main
import org.eclipse.xtend.lib.annotations.Accessors

/**
 * The base class for all logic. All logic gates MUST be extend this class<br />
 * Signal I/O and connections is handled in this class automatically, can be overridden to take control of
 * the Signal I/O, or the connections (i.e, randomizer), however, it is probably not necessary
 * @category LogicGates
 * @see Signal
 */
abstract class LogicGate implements ILogicInput, ILogicOutput{
	@Accessors(PUBLIC_GETTER, PROTECTED_SETTER)
	int inputs = 0 
	@Accessors(PUBLIC_GETTER, PROTECTED_SETTER)
	int outputs = 0
	@Accessors(PROTECTED_GETTER, PROTECTED_SETTER)
	ArrayList<Signal> outputList = newArrayList()
	@Accessors(PROTECTED_GETTER, PROTECTED_SETTER)
	ArrayList<Signal> inputList = newArrayList()
	@Accessors(PROTECTED_GETTER, PROTECTED_SETTER)
	ArrayList<List<ILogicOutput>> inputListeners = newArrayList()
	@Accessors(PROTECTED_GETTER, PROTECTED_SETTER)
	ArrayList<List<ILogicInput>> outputListeners = newArrayList()
	@Accessors
	Rectangle area = new Rectangle
	static val backgroundImage = Toolkit.getDefaultToolkit.createImage(typeof(Main).classLoader.getResource("assets/logicBackground.png"))
	
	def protected void setOutput(int port, Signal signal){
		try{
			this.outputList.set(port, signal)
		} catch(IndexOutOfBoundsException e){
			if(port < inputs){
				do{
					this.outputList.add(new Signal(false, 0.0))
				} while(this.outputList.size < port)
				this.outputList.add(signal)
			} else {
				throw new IndexOutOfBoundsException("Port specified is greater than outputs!\n"+
						"(Did you forget it's 0 based?)")
			}
		}
	}
	
	def getOutput(int port){
		this.outputList.get(port)
	}
	
	def void setInput(int port, Signal signal){
		try{
			this.inputList.set(port, signal)
		} catch(IndexOutOfBoundsException e){
			if(port < inputs){
				do{
					this.inputList.add(new Signal(false, 0.0))
				} while(this.inputList.size < port)
				this.inputList.add(signal)
			} else {
				throw new IndexOutOfBoundsException("Port specified is greater than inputs!\n"+
						"(Did you forget it's 0 based?)")
			}
		}
	}
	
	def protected getInput(int port){
		this.inputList.get(port)
	}
	
	override inputBroken(int port, ILogicInput listener) {
		this.outputListeners.get(port).remove(listener)
	}
	
	override outputBroken(int port, ILogicOutput listener) {
		this.inputListeners.get(port).remove(listener)
	}
	
	override inputSignal(int port, Signal s) {
		setInput(port, s);
	}
	
	def void update();
	
	def Image getImage();
	
	def getBackgroundImage(){
		backgroundImage
	}
	
	/**
	 * Called when a logic gate is destroyed to handle any cleanup (like breaking connections).
	 * @return True if successful, false if canceled
	 */
	def boolean destory(){
		this.inputListeners.forEach[
			for(var x = 0; x < length; x++){
				get(x).inputBroken(x, this);
			}
		]
		this.outputListeners.forEach[
			for(var x = 0; x < length; x++){
				get(x).outputBroken(x, this)
			}
		]
		return true;
	}
}