package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore ts1;
  private TorpedoStore ts2;

  @BeforeEach
  public void init(){
    this.ts1 = mock(TorpedoStore.class);
    this.ts2 = mock(TorpedoStore.class);
    this.ship = new GT4500(ts1, ts2);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(this.ts1.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(ts1, times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_FirePrimaryFirst() {
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(ts1, times(1)).fire(1);
    verify(ts2, never()).fire(anyInt());
  }

  @Test
  public void fireTorpedo_All_FireBoth() {
    ship.fireTorpedo(FiringMode.ALL);

    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_Alternating() {
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(ts1, times(1)).fire(1);
    verify(ts2, never()).fire(anyInt());

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_PrimaryEmpty() {
    when(ts1.isEmpty()).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);

    verify(ts1, never()).fire(anyInt());
    verify(ts2, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_SecondaryEmptySecondFire() {
    when(ts2.isEmpty()).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(ts1, times(2)).fire(1);
    verify(ts2, never()).fire(anyInt());
  }

  @Test
  public void fireTorpedo_Single_BothEmpty() {
    when(ts1.isEmpty()).thenReturn(true);
    when(ts2.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    verify(ts1, never()).fire(anyInt());
    verify(ts2, never()).fire(anyInt());
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_All_BothFail() {
    when(ts1.fire(1)).thenReturn(false);
    when(ts2.fire(1)).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);

    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_All_OneFail() {
    when(ts1.fire(1)).thenReturn(false);
    when(ts2.fire(1)).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(1);

    assertEquals(true, result);

    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenReturn(false);

    result = ship.fireTorpedo(FiringMode.ALL);

    verify(ts1, times(2)).fire(1);
    verify(ts2, times(2)).fire(1);

    assertEquals(true, result);
  }
}
