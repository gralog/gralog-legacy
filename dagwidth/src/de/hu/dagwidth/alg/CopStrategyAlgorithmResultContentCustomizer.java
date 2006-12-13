/*
 * Created on 10 Nov 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.hu.dagwidth.alg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Customizer;

import javax.swing.JButton;
import javax.swing.JPanel;

public class CopStrategyAlgorithmResultContentCustomizer extends JPanel implements Customizer, ActionListener {

	private CopStrategyAlgorithmResultContent content = null;
	
	private static final String NEW_GAME_BUTTON = "New Game";
	private static final String PREVIOUS_POSITION_BUTTON = "<";
	
	private JButton newGame = new JButton( NEW_GAME_BUTTON );
	private JButton prevPosition = new JButton( PREVIOUS_POSITION_BUTTON );
	
	public CopStrategyAlgorithmResultContentCustomizer() {
		super();
		
		newGame.setActionCommand( NEW_GAME_BUTTON );
		newGame.addActionListener( this );
		add( newGame );

		prevPosition.setActionCommand( PREVIOUS_POSITION_BUTTON );
		prevPosition.addActionListener( this );
		add( prevPosition );
		updateControls();
	}
	
	public void setObject(Object bean) {
		content = (CopStrategyAlgorithmResultContent) bean;
	}

	public void actionPerformed(ActionEvent e) {
		if ( e.getActionCommand().equals( NEW_GAME_BUTTON ) ) 
			content.newGame();
		if ( e.getActionCommand().equals( PREVIOUS_POSITION_BUTTON ) ) {
			if ( content.hasPreviosPosition() )
				content.prevPosition();
		}
	}
	
	private void updateControls() {
	}

}
