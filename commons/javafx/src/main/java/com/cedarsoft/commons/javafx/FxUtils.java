package com.cedarsoft.commons.javafx;

import java.io.PrintStream;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FxUtils {
  /**
   * Used for reflection workaround
   */
  private static final boolean IS_JDK_8 = System.getProperty("java.version").contains("1.8");

  /**
   * Returns the currently focused stage, or the one open stage or null if there is no stage or there are multiple stages
   */
  public static Stage getStage() {
    @Nullable Stage stageSafe = getStageSafe();
    if (stageSafe == null) {
      throw new IllegalStateException("No stage found");
    }

    return stageSafe;
  }

  /**
   * returns the stage (or null) if there is no stage
   */
  @Nullable
  public static Stage getStageSafe() {
    List<? extends Stage> stages = getStages();

    //Find the focused stage
    for (Stage stage : stages) {
      if (stage.isFocused()) {
        return stage;
      }
    }

    //we have no focused stage, therefore return the one stage if there is only one

    if (stages.size() == 1) {
      return stages.get(0);
    }

    //no stage found
    return null;
  }

  /**
   * Returns the available stages
   */
  public static List<? extends Stage> getStages() {
    try {

      if (IS_JDK_8) {
        //Code for JDK 8

        //Use reflection since StageHelper is not accesible with JDK > 8
        //
        // Replaces that code:
        // return StageHelper.getStages()
        Class<?> stageHelper = Class.forName("com.sun.javafx.stage.StageHelper");
        //noinspection unchecked
        return (List<? extends Stage>) stageHelper.getDeclaredMethod("getStages").invoke(null);
      }
      //Code for JDK 11

      //Use reflection since the method does not exist in JDK < 9
      //
      // Replaces that code:
      //windows = Stage.getWindows()

      //noinspection unchecked
      List<? extends Window> windows = (List<? extends Window>) Stage.class.getMethod("getWindows").invoke(null);

      return windows.stream()
               .filter(window -> window instanceof Stage)
               .map(window -> (Stage) window)
               .collect(ImmutableList.toImmutableList());

    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void dump(Node node, PrintStream out) {
    dump(node, out, 0);
  }

  private static void dump(Node node, PrintStream out, int depth) {
    out.println(Strings.repeat("  ", depth) + node + " #" + node.getId());

    Parent parent = (Parent) node;
    parent.getChildrenUnmodifiable()
      .forEach(child -> {
        dump(child, out, depth + 1);
      });
  }
}
