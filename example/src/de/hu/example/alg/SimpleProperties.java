package de.hu.example.alg;

import de.hu.gralog.algorithm.Algorithm;
import de.hu.gralog.algorithm.InvalidPropertyValuesException;
import de.hu.gralog.algorithm.result.AlgorithmResult;
import de.hu.gralog.app.UserException;

/**
 * This example shows how to define an Algorithm
 * that has a <b>name</b>, a <b>description</b> and
 * takes parameters that have simple values such as
 * <b>int</b>, <b>double</b>, <b>boolean</b> and <b>String</b>. 
 * 
 * This example shows how to define a <b>BeanInfo</b>-class for
 * a {@link de.hu.gralog.algorithm Gralog-Algorithm} in order
 * to define a description and a name for this Algorithm and
 * all it's properties.
 * 
 * Please see {SimplePropertiesBeanInfo} for the definition of
 * this algorithm's <b>BeanInfo</b>-class and {@link de.hu.gralog.beans} for
 * more information about the usage of JavaBeans in GrALoG, respectively
 * {@link de.hu.gralog.algorithm} for more information about
 * algorithms in GrALoG.
 * 
 * @author Sebastian
 *
 */
public class SimpleProperties implements Algorithm {

	/**
	 * These are the properties of this algorithm.
	 * 
	 */
	private int anInt;
	private double aDouble;
	private boolean aBoolean;
	private String aString;
	

	/**
	 * The getter-function for <b>aBoolean</b>
	 * 
	 * @return the value of <b>aBoolean</b>
	 */
	public boolean isABoolean() {
		return aBoolean;
	}

	/**
	 * The setter-function for <b>aBoolean</b>
	 * 
	 * @param aBoolean the new value for aBoolean
	 */
	public void setABoolean(boolean aBoolean) {
		this.aBoolean = aBoolean;
	}

	/**
	 * The getter-function for <b>aBoolean</b>
	 * 
	 * @return the value of <b>aBoolean</b>
	 */
	public double getADouble() {
		return aDouble;
	}

	/**
	 * The setter-function for <b>aDouble</b>
	 * 
	 * @param aDouble the new value for aDouble
	 */
	public void setADouble(double aDouble) {
		this.aDouble = aDouble;
	}

	/**
	 * The getter-function for <b>anInt</b>
	 * 
	 * @return the value of <b>anInt</b>
	 */
	public int getAnInt() {
		return anInt;
	}

	/**
	 * The setter-function for <b>anInt</b>
	 * 
	 * @param anInt the new value for anInt
	 */
	public void setAnInt(int anInt) {
		this.anInt = anInt;
	}

	/**
	 * The getter-function for <b>aString</b>
	 * 
	 * @return the value of <b>aString</b>
	 */
	public String getAString() {
		return aString;
	}

	/**
	 * The setter-function for <b>aString</b>
	 * 
	 * @param aString the new value for aString
	 */
	public void setAString(String aString) {
		this.aString = aString;
	}

	/**
	 * The execute-function of this algorithm, that in this
	 * case does nothing but to check constrains for the given
	 * parameters and then returns null.
	 * 
	 */
	public AlgorithmResult execute() throws InvalidPropertyValuesException,
			UserException {
		InvalidPropertyValuesException e = new InvalidPropertyValuesException();
		if ( getAnInt() < 0 )
			e.addPropertyError( "anInt", InvalidPropertyValuesException.GREATER_EQUAL_ZERO );
		if ( getADouble() < 0 || getADouble() > 1 )
			e.addPropertyError( "aDouble", "aDouble must be between 0 and 1" );
		if ( getAString() == null )
			e.addPropertyError( "aString", InvalidPropertyValuesException.PROPERTY_REQUIRED );
		if ( e.hasErrors() )
			throw e;
		
		return null;
	}

}
