package jp.hashiwa.reversi.test;

import static org.junit.Assert.*;
import org.junit.Test;

import jp.hashiwa.reversi.frame.RCell;
import jp.hashiwa.reversi.frame.RCell.State;

public class RCellTest {

  @Test
  public void testInitialState() {
    RCell cell = new RCell(0, 0, 5);
    assertEquals(State.None, cell.getState());
  }

  @Test
  public void testSetState() {
    RCell cell = new RCell(0, 0, 5);
    
    cell.setState(State.Black);
    assertEquals(State.Black, cell.getState());
    
    cell.setState(State.White);
    assertEquals(State.White, cell.getState());
    
    cell.setState(State.None);
    assertEquals(State.None, cell.getState());
  }

  @Test
  public void testClone() {
    RCell original = new RCell(1, 2, 5);
    original.setState(State.Black);
    
    RCell cloned = original.clone();
    assertEquals(original.getXIndex(), cloned.getXIndex());
    assertEquals(original.getYIndex(), cloned.getYIndex());
    assertEquals(original.getState(), cloned.getState());
    
    // クローン後の変更がオリジナルに影響しないこと
    cloned.setState(State.White);
    assertEquals(State.Black, original.getState());
  }
}
