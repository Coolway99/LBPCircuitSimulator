package main.interfaces;

import java.awt.Rectangle;

import javax.swing.Icon;

/**The base interface that ALL logic gates MUST implement
 * @category LogicGates*/
public interface ILogicGate {
	/**Returns the number of inputs
	 * @return The number of inputs*/
	public int getInputs();
	/**If the number of inputs are adjustable, this should return true, otherwise false
	 * @return Are the number of inputs changeable*/
	public boolean areInputsSettable();
	/**Sets the number of inputs (shouldn't be called if areInputsSettable returns false
	 *@see this.areInputsSettable()
	 *@param value The new amount of inputs*/
	public void setInputs(int value);
	/**Gets the maximum number of inputs
	 * @return The maximum number of inputs*/
	public int getInputMaxValue();
	/**Gets the minimum number of inputs
	 * @return The minimum number of inputs*/
	public int getInputMinValue();
	/**Returns the number of outputs
	 * @return The number of outputs*/
	public int getOutputs();
	/**If the number of outputs are adjustable, this should return true, otherwise false
	 * @return Are the number of outputs changeable*/
	public boolean areOutputsSettable();
	/**Sets the number of outputs (shouldn't be called if areInputsSettable returns false)
	 *@see this.areInputsSettable()
	 *@param value The new amount of outputs*/
	public void setOutputs(int value);
	/**Gets the maximum number of outputs
	 * @return The maximum number of outputs*/
	public int getOutputMaxValue();
	/**Gets the minimum number of outputs
	 * @return The minimum number of outputs*/
	public int getOutputMinValue();
	/**If there are inputs in the bottom (like the microchip and selector), this should return true
	 * @return If it has bottom inputs are not*/
	public boolean hasBottomInputs();
	/**Returns the number of bottom inputs
	 * @return The number of bottom inputs*/
	public int getBottomInputs();
	/**Used to get the size for the object
	 * @return A rectangle representing the size*/
	public Rectangle getGateSize();
	/**Called when the gate is going to get deleted*/
	public void destroyGate();
	/**Called when this gate makes a connection to another*/
	public void connectOutput(int port, ILogicGateComponent toGate);
	/**Called when another gate makes a connection to this one*/
	public void connectInput(int port, ILogicGateComponent fromGate);
	/**Used for getting the image that is stretched on the background of the gate (the outline),
	 * usually a set default image<br />
	 * The resolution is 20x20 pixels, however make it as big as you wish, it will be
	 * rendered at runtime at the correct stretched size
	 * @return The icon used for the back
	 * @see getForegroundIcon
	 * */
	public Icon getBackgroundIcon();
	/**Used for getting the image that is static in the middle of the object. Usually a symbol
	 * representing it.
	 * The resolution it is rendered at is 15x15 pixels, however if it is bigger/smaller it will be resized
	 * The image should be white as it will be hue changed to the colors set
	 * @return The icon used for the front
	 * @see getBackgroundIcon
	 */
	public Icon getForegroundIcon();
}
