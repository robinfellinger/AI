package tddc17;

import aima.core.environment.liuvacuum.*;
import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

class MyAgentState {
	public int[][] world = new int[30][30];
	public int initialized = 0;
	final int UNKNOWN = 0;
	final int WALL = 1;
	final int CLEAR = 2;
	final int DIRT = 3;
	final int HOME = 4;
	final int ACTION_NONE = 0;
	final int ACTION_MOVE_FORWARD = 1;
	final int ACTION_TURN_RIGHT = 2;
	final int ACTION_TURN_LEFT = 3;
	final int ACTION_SUCK = 4;

	public int agent_x_position = 1;
	public int agent_y_position = 1;
	public int agent_last_action = ACTION_NONE;

	public Queue<Integer> actionsQueue = new LinkedList<>();
	public ArrayList<Point> path = new ArrayList<>();

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public int agent_direction = EAST;

	MyAgentState() {
		for (int i = 0; i < world.length; i++)
			for (int j = 0; j < world[i].length; j++)
				world[i][j] = UNKNOWN;
		world[1][1] = HOME;
		agent_last_action = ACTION_NONE;
	}

	// Based on the last action and the received percept updates the x & y agent
	// position
	public void updatePosition(DynamicPercept p) {
		Boolean bump = (Boolean) p.getAttribute("bump");

		if (agent_last_action == ACTION_MOVE_FORWARD && !bump) {
			switch (agent_direction) {
			case MyAgentState.NORTH:
				agent_y_position--;
				break;
			case MyAgentState.EAST:
				agent_x_position++;
				break;
			case MyAgentState.SOUTH:
				agent_y_position++;
				break;
			case MyAgentState.WEST:
				agent_x_position--;
				break;
			}
		}

	}

	public void updateWorld(int x_position, int y_position, int info) {
		world[x_position][y_position] = info;
	}

	public void printWorldDebug() {
		for (int i = 0; i < world.length; i++) {
			for (int j = 0; j < world[i].length; j++) {
				if (world[j][i] == UNKNOWN)
					System.out.print(" ? ");
				if (world[j][i] == WALL)
					System.out.print(" # ");
				if (world[j][i] == CLEAR)
					System.out.print(" X ");
				if (world[j][i] == DIRT)
					System.out.print(" D ");
				if (world[j][i] == HOME)
					System.out.print(" H ");
			}
			System.out.println("");
		}
	}
}

class MyAgentProgram implements AgentProgram {

	private int initnialRandomActions = 10;
	private Random random_generator = new Random();

	// Here you can define your variables!

	public MyAgentState state = new MyAgentState();
	public int iterationCounter = state.world.length * state.world[0].length * 2;
	public boolean initialState = true;
	public int depth = 0;
	public boolean goingHome = false;
	public static final int FORWARD = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;

	// moves the Agent to a random start position
	// uses percepts to update the Agent position - only the position, other
	// percepts are ignored
	// returns a random action
	private Action moveToRandomStartPosition(DynamicPercept percept) {
		int action = random_generator.nextInt(6);
		initnialRandomActions--;
		state.updatePosition(percept);
		if (action == 0) {
			state.agent_direction = ((state.agent_direction - 1) % 4);
			if (state.agent_direction < 0)
				state.agent_direction += 4;
			state.agent_last_action = state.ACTION_TURN_LEFT;
			return LIUVacuumEnvironment.ACTION_TURN_LEFT;
		} else if (action == 1) {
			state.agent_direction = ((state.agent_direction + 1) % 4);
			state.agent_last_action = state.ACTION_TURN_RIGHT;
			return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
		}
		state.agent_last_action = state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}

	public Action turnLeft() {
		state.agent_direction = ((state.agent_direction - 1) % 4);
		if (state.agent_direction < 0)
			state.agent_direction += 4;
		state.agent_last_action = state.ACTION_TURN_LEFT;
		return LIUVacuumEnvironment.ACTION_TURN_LEFT;
	}

	public Action turnRight() {
		state.agent_last_action = state.ACTION_TURN_RIGHT;
		state.agent_direction = ((state.agent_direction + 1) % 4);
		return LIUVacuumEnvironment.ACTION_TURN_RIGHT;
	}

	public Action suck() {
		System.out.println("DIRT -> choosing SUCK action!");
		state.agent_last_action = state.ACTION_SUCK;
		return LIUVacuumEnvironment.ACTION_SUCK;
	}

	public Action moveForward() {
		state.agent_last_action = state.ACTION_MOVE_FORWARD;
		return LIUVacuumEnvironment.ACTION_MOVE_FORWARD;
	}
	
	
	public void goNorth() {
		switch (state.agent_direction) {
		case 0: 
			break;
		case 1:
			state.actionsQueue.add(LEFT);
			break;
		case 2:
			state.actionsQueue.add(LEFT);
			state.actionsQueue.add(LEFT);
			break;	
		case 3:
			state.actionsQueue.add(RIGHT);
			break;	
		}
		state.actionsQueue.add(FORWARD);
	}
	
	public void goEast() {
		switch (state.agent_direction) {
		case 0: 
			state.actionsQueue.add(RIGHT);
			break;
		case 1:
			break;
		case 2:
			state.actionsQueue.add(LEFT);
			break;	
		case 3:
			state.actionsQueue.add(RIGHT);
			state.actionsQueue.add(RIGHT);
			break;	
		}
		state.actionsQueue.add(FORWARD);
	}
	
	public void goSouth() {
		switch (state.agent_direction) {
		case 0: 
			state.actionsQueue.add(RIGHT);
			state.actionsQueue.add(RIGHT);
			break;
		case 1:
			state.actionsQueue.add(RIGHT);
			break;
		case 2:
			break;	
		case 3:
			state.actionsQueue.add(LEFT);
			break;	
		}
		state.actionsQueue.add(FORWARD);
	}
	
	public void goWest() {
		switch (state.agent_direction) {
		case 0: 
			state.actionsQueue.add(LEFT);
			break;
		case 1:
			state.actionsQueue.add(RIGHT);
			state.actionsQueue.add(RIGHT);
			break;
		case 2:
			state.actionsQueue.add(RIGHT);
			break;	
		case 3:
			break;	
		}
		state.actionsQueue.add(FORWARD);
	}
	

//	private void uTurnRight() {
//		state.actionsQueue.add(FORWARD);
//		state.actionsQueue.add(LEFT);
//		state.actionsQueue.add(FORWARD);
//		state.actionsQueue.add(RIGHT);
//		state.actionsQueue.add(RIGHT);
//	}
//
//	private void uTurnLeft() {
//		state.actionsQueue.add(FORWARD);
//		state.actionsQueue.add(RIGHT);
//		state.actionsQueue.add(FORWARD);
//		state.actionsQueue.add(LEFT);
//		state.actionsQueue.add(LEFT);
//	}

	public Action getAction() {
		switch (state.actionsQueue.remove()) {
		case 1:
			return moveForward();
		case 2:
			return turnRight();
		case 3:
			return turnLeft();
		default:
			return null;
		}
	}

	public ArrayList<Point> getPath() {
		Queue<Point> frontier = new LinkedList<Point>();

		Point start = new Point(state.agent_x_position, state.agent_y_position);
		frontier.add(start); // adds current position as frontier

		Map<Point, Point> links = new HashMap<Point, Point>();

		while (!frontier.isEmpty()) {
			Point currentPoint = frontier.remove();

			int x = currentPoint.x;
			int y = currentPoint.y;

			// if the path to the point is unknown, calculate the path
			if (state.world[x][y] == state.UNKNOWN && x >= 0 && y >= 0) {
				return calcPath(links, currentPoint);
			}

			// checking the 4 neighbor points
			for (int dX = -1; dX < 1; dX++) {
				for (int dY = -1; dY < 1; dY++) {
					Point nextPoint = new Point(x + dX, y + dY);

					if (!links.containsKey(nextPoint) && Math.abs(dX) != Math.abs(dY)
							&& state.world[nextPoint.x][nextPoint.y] != state.WALL) {
						links.put(nextPoint, currentPoint);
						frontier.add(nextPoint);
					}
				}
			}

		}
		return null;

	}

	public ArrayList<Point> calcPath(Map<Point, Point> links, Point currentPoint) {
		ArrayList<Point> path = new ArrayList<Point>();
		path.add(currentPoint);
		// Point currentPoint = goal;
		// todo
		while ((currentPoint = links.get(currentPoint)) != null) {
			path.add(currentPoint);
		}
		// todo
		path.remove(path.size() - 1);
		return path;
	}

	@Override
	public Action execute(Percept percept) {

		// DO NOT REMOVE this if condition!!!
		if (initnialRandomActions > 0) {
			return moveToRandomStartPosition((DynamicPercept) percept);
		} else if (initnialRandomActions == 0) {
			// process percept for the last step of the initial random actions
			initnialRandomActions--;
			state.updatePosition((DynamicPercept) percept);
			System.out.println("Processing percepts after the last execution of moveToRandomStartPosition()");
			state.agent_last_action = state.ACTION_SUCK;
			return LIUVacuumEnvironment.ACTION_SUCK;
		}

		// This example agent program will update the internal agent state while only
		// moving forward.
		// START HERE - code below should be modified!

		System.out.println("x=" + state.agent_x_position);
		System.out.println("y=" + state.agent_y_position);
		System.out.println("dir=" + state.agent_direction);
		System.out.println("goingHome=" + goingHome);
		System.out.println("iterationCounter=" + iterationCounter);

		iterationCounter--;

		if (iterationCounter == 0)
			return NoOpAction.NO_OP;

		DynamicPercept p = (DynamicPercept) percept;
		Boolean bump = (Boolean) p.getAttribute("bump");
		Boolean dirt = (Boolean) p.getAttribute("dirt");
		Boolean home = (Boolean) p.getAttribute("home");
		System.out.println("percept: " + p);
		System.out.println(initialState);

		// State update based on the percept value and the last action
		state.updatePosition((DynamicPercept) percept);

//		if (bump) {
//			switch (state.agent_direction) {
//			case MyAgentState.NORTH:
//				state.updateWorld(state.agent_x_position, state.agent_y_position - 1, state.WALL);
//				break;
//			case MyAgentState.EAST:
//				state.updateWorld(state.agent_x_position + 1, state.agent_y_position, state.WALL);
//				break;
//			case MyAgentState.SOUTH:
//				state.updateWorld(state.agent_x_position, state.agent_y_position + 1, state.WALL);
//				break;
//			case MyAgentState.WEST:
//				state.updateWorld(state.agent_x_position - 1, state.agent_y_position, state.WALL);
//				break;
//			}
//		}
//		if (dirt)
//			state.updateWorld(state.agent_x_position, state.agent_y_position, state.DIRT);
//		else
//			state.updateWorld(state.agent_x_position, state.agent_y_position, state.CLEAR);
//
//		state.printWorldDebug();

		// Next action selection based on the percept value

		if (dirt) {
			return suck();
		}

		if (state.actionsQueue.isEmpty()) {// get new points in the queue
			if (state.path.isEmpty()) {
				state.path = getPath();
//				if (state.path.isEmpty()) { // all is explored
//					return NoOpAction.NO_OP;
//				}

			} else {
				Point currentPoint = new Point(state.agent_x_position, state.agent_y_position);
				Point next = state.path.remove(state.path.size() - 1);

				int dir = 0;

				if (next.y < currentPoint.y) {
					dir = 0;
				} else if (next.y > currentPoint.y) {
					dir = 2;
				} else if (next.x > currentPoint.x) {
					dir = 1;
				} else if (next.x < currentPoint.x) {
					dir = 3;
				}

				switch (dir) {
				case 0:
					goNorth();
					break;
				case 1:
					goEast();
					break;
				case 2:
					goSouth();
					break;
				case 3:
					goWest();
					break;
				}
			}

		}
			
			if (!state.actionsQueue.isEmpty()) {
			return getAction();
		}
		return null;
	}

}

public class MyVacuumAgent extends AbstractAgent {
	public MyVacuumAgent() {
		super(new MyAgentProgram());
	}
}
