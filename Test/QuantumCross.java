import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class QuantumCross extends JComponent implements MouseListener{

	public static void main(String[] args){
		JFrame frm = new JFrame("Quantim Noughts-and-Crosses");
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = frm.getContentPane();
		c.setLayout(new BorderLayout());
		QuantumCross qc = new QuantumCross(true);
		c.addMouseListener(qc);
		c.add(BorderLayout.CENTER, qc);
		frm.setContentPane(c);
		frm.pack();
		frm.setVisible(true);
	}

	/*
	 * Constants
	 */
	public static final int BORDER = 50;
	public static final int BOX = 150;
	//thicknesses of graphics stroke
	public static final int STROKE_GRID = 6;
	public static final int STROKE_TANGLE = 1;
	//stages of game
	public static final int STAGE_NONE = -1;
	public static final int STAGE_A = 0;
	public static final int STAGE_B = 1;
	public static final int STAGE_C = 2;
	//colours
	public static final Color COL_BG = Color.BLACK;
	public static final Color COL_GRID = Color.GRAY;
	public static final Color COL_UP = Color.RED;
	public static final Color COL_DOWN = Color.BLUE;

	/*
	 * Attributes
	 */
	private boolean upSpinStarts;
	private boolean upTurn;
	private int stage;
	private int qWinner;
	private int cWinner;
	private int collapseDecision;
	//if both moves are positive, the waveform is not collapsed
	//negate the side of the waveform which is NOT observed
	private ArrayList<Integer> moveA;
	private ArrayList<Integer> moveB;

	public QuantumCross(boolean startWithUp){
		clearField(startWithUp);
	}
	
	public void clearField(boolean upStarts){
		upSpinStarts = upStarts;
		upTurn = upSpinStarts;
		stage = STAGE_A;
		collapseDecision = -1;
		moveA = new ArrayList<>();
		moveB = new ArrayList<>();
	}

	private Dimension entangledPos(int thisState, int altCount){
		Dimension colPos = collapsedPos(thisState);
		if(altCount > 3) altCount++;
		return new Dimension(colPos.width + (2*(altCount%3) - 2)*BOX/6, colPos.height + (2*(altCount/3) - 2)*BOX/6); 
	}
	private Dimension collapsedPos(int thisState){
		return new Dimension(BORDER + BOX*((thisState-1)%3) + BOX/2, BORDER + BOX*((thisState-1)/3) + BOX/2); 
	}
	
	private void drawMove(boolean upMove, boolean fullSize, int order, Dimension pos, Graphics2D g2){
		g2.setColor(upMove ? COL_UP : COL_DOWN);
		int len = fullSize ? BOX : BOX/3;
		g2.fillOval(pos.width-len/2, pos.height-len/2, len, len);
	}
	
	private boolean checkIfWins(boolean[][] moves){
		for(int r=0; r<3; r++) if(moves[r][0] && moves[r][1] && moves[r][2]) return true;
		for(int c=0; c<3; c++) if(moves[0][c] && moves[1][c] && moves[2][c]) return true;
		if(moves[0][0] && moves[1][1] && moves[2][2]) return true;
		if(moves[0][2] && moves[1][1] && moves[2][0]) return true;
		return false;
	}
	
	private boolean checkForWinner(){
		boolean[][][] moveMarks = new boolean[2][3][3];
		int quantumWinner = -1;
		int classicWinner = -1;
		boolean upMove = upSpinStarts;
		for(int i=0; i<moveB.size(); i++){
			if(moveA.get(i) < 0 || moveB.get(i) < 0){
				int move = moveA.get(i) < 0 ? moveB.get(i) : moveA.get(i);
				moveMarks[upMove ? 0 : 1][(move-1)/3][(move-1)%3] = true;
			}
			boolean upWins = checkIfWins(moveMarks[0]);
			boolean downWins = checkIfWins(moveMarks[0]);
			if(upWins){
				classicWinner = 0;
				if(quantumWinner == -1) quantumWinner = 0;
				if(downWins) classicWinner = -1;
			} else if (downWins){
				classicWinner = 1;
				if(quantumWinner == -1) quantumWinner = 1; 
			}
			upMove = !upMove;
		}
		if(quantumWinner == -1){
			return false;
		} else {
			qWinner = quantumWinner;
			cWinner = classicWinner;
			System.out.println(qWinner + " - " + cWinner);
			stage = STAGE_NONE;
			return true;
		}
	}
	
	private void nextTurn(){
		if(!checkForWinner()){
			stage = STAGE_A;
			upTurn = !upTurn;
			collapseDecision = -1;
		}
	}
	
	private int owner(int sq){
		boolean upOwns = upSpinStarts;
		for(int i=0; i<moveB.size(); i++){
			if((moveA.get(i) == sq && moveB.get(i) < 0) || (moveB.get(i) == sq && moveA.get(i) < 0)){
				return upOwns ? 0 : 1;
			}
			upOwns = !upOwns;
		}
		return -1;
	}
	
	private boolean owned(int sq){
		return owner(sq) != -1;
	}
	
	private int collapseCycle(){
		HashSet<Integer> positions = new HashSet<>();
		HashSet<Integer> edges = new HashSet<>();
		int ret = moveA.size()-1;
		edges.add(ret);
		positions.add(moveA.get(ret));
		positions.add(moveB.get(ret));
		Stack<Integer> sta = new Stack<>();
		sta.push(moveA.get(ret));
		sta.push(moveB.get(ret));
		while(!sta.isEmpty()){
			int point = sta.pop();
			for(int i=0; i<moveA.size(); i++) if(!edges.contains(i)){
				if(moveA.get(i) == point || moveB.get(i) == point){
					edges.add(i);
					int other = moveA.get(i);
					if(other == point) other = moveB.get(i);
					if(positions.contains(other)) return ret;
					sta.push(other);
				}
			}
		}
		return -1;
	}
	
	private void collapse(int decision, boolean observeA){
		int other = moveB.get(decision);
		if(observeA){
			moveB.set(decision, -other);
			//System.out.println("Collapsing " + decision + " to " + moveA.get(decision));
		} else {
			other = moveA.get(decision);
			moveA.set(decision, -other);
			//System.out.println("Collapsing " + decision + " to " + moveB.get(decision));
		}
		for(int i=0; i<moveA.size(); i++){
			if(moveA.get(i) == other && moveB.get(i) > 0){
				collapse(i, true);
			} else if (moveB.get(i) == other && moveA.get(i) > 0){
				collapse(i, false);
			}
		}
	}

	/*
	 * Mouse interface events
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent me) {
		int sq = 0;
		if(me.getX() > BORDER && me.getX() < BORDER + 3*BOX && me.getY() > BORDER && me.getY() < BORDER + 3*BOX){
			sq = 1 + ((me.getX()-BORDER)/BOX) + (3*((me.getY()-BORDER)/BOX));
		}
		switch(stage){
		case STAGE_A :
			if(sq != 0 && !owned(sq)){
				moveA.add(sq);
				stage = STAGE_B;
			}
			break;
		case STAGE_B :
			if(sq != 0 && !owned(sq)){
				if(sq == moveA.get(moveA.size()-1)){
					moveA.remove(moveA.size()-1);
					stage = STAGE_A;
				} else {
					moveB.add(sq);
					collapseDecision = collapseCycle();
					if(collapseDecision != -1){
						stage = STAGE_C;
					} else {
						nextTurn();
					}
				}
			}
			break;
		case STAGE_C :
			if(sq == moveA.get(collapseDecision)){
				collapse(collapseDecision, true);
				nextTurn();
			} else if(sq == moveB.get(collapseDecision)){
				collapse(collapseDecision, false);
				nextTurn();
			}
			break;
		default :
			clearField(qWinner == 0);
			break;
		}
		repaint();
	}

	/*
	 * Methods inherited from the JComponent
	 */
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(2*BORDER + 3*BOX, 2*BORDER + 3*BOX);
	}
	@Override
	public Dimension getMinimumSize(){return getPreferredSize();}
	@Override
	public Dimension getMaximumSize(){return getPreferredSize();}
	@Override
	public void paint(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(COL_BG);
		g2.fillRect(0, 0, 2*BORDER + 3*BOX, 2*BORDER + 3*BOX);
		g2.setStroke(new BasicStroke(STROKE_GRID));
		if(stage == STAGE_C){
			g2.setColor(!upTurn ? COL_UP : COL_DOWN);
		} else if(stage == STAGE_A || stage == STAGE_B){
			g2.setColor(upTurn ? COL_UP : COL_DOWN);
		}
		g2.drawRect(STROKE_GRID/2,  STROKE_GRID/2, 2*BORDER + 3*BOX - STROKE_GRID, 2*BORDER + 3*BOX - STROKE_GRID);
		//draw the entanglements
		g2.setStroke(new BasicStroke(STROKE_TANGLE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		boolean upSpinGo = upSpinStarts;
		for(int i=0; i<moveB.size(); i++){
			if(moveA.get(i) > 0 && moveB.get(i) > 0) {
				g2.setColor(upSpinGo ? COL_UP : COL_DOWN);
				Dimension aPos = collapsedPos(moveA.get(i));
				Dimension bPos = collapsedPos(moveB.get(i));
				g2.draw(new QuadCurve2D.Float(aPos.width, aPos.height, 
						(aPos.width + bPos.width)/2 + (upSpinGo ? -BOX/2 : BOX/2), (aPos.height + bPos.height)/2 + (upSpinGo ? -BOX/3 : BOX/3), 
						bPos.width, bPos.height));
			}
			upSpinGo = !upSpinGo;
		}
		//draw the grid
		g2.setStroke(new BasicStroke(STROKE_GRID, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.setColor(COL_GRID);
		for(int i=1; i<3; i++) g2.drawLine(i*BOX + BORDER, BORDER, i*BOX + BORDER, 3*BOX+BORDER);
		for(int i=1; i<3; i++) g2.drawLine(BORDER, i*BOX + BORDER, 3*BOX+BORDER, i*BOX + BORDER);
		if(stage == STAGE_C){
			g2.setColor(!upTurn ? COL_UP : COL_DOWN);
			Dimension aLoc = collapsedPos(moveA.get(collapseDecision));
			g2.drawRect(aLoc.width - BOX/2, aLoc.height-BOX/2, BOX, BOX);
			Dimension bLoc = collapsedPos(moveB.get(collapseDecision));
			g2.drawRect(bLoc.width - BOX/2, bLoc.height-BOX/2, BOX, BOX);
		}
		//draw the moves
		g2.setStroke(new BasicStroke(STROKE_TANGLE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		upSpinGo = upSpinStarts;
		int[] altCounts = new int[10];
		for(int i=0; i<moveA.size(); i++){
			if(i >= moveB.size() || (moveA.get(i) > 0 && moveB.get(i) > 0)) {//entangled
				drawMove(upSpinGo, false, i, entangledPos(moveA.get(i), altCounts[moveA.get(i)]), g2);
				altCounts[moveA.get(i)]++;
				if(i < moveB.size()){
					drawMove(upSpinGo, false, i, entangledPos(moveB.get(i), altCounts[moveB.get(i)]), g2);
					altCounts[moveB.get(i)]++;
				}
			}
			else{//collapsed
				drawMove(upSpinGo, true, i, collapsedPos(moveA.get(i) > 0 ? moveA.get(i) : moveB.get(i)), g2);
			}
			upSpinGo = !upSpinGo;
		}
	}
}