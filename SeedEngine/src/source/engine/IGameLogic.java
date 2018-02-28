package source.engine;

import source.render.DisplayManager;

/**
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public interface IGameLogic {

    String getTitle();

    void init() throws Exception;

    void input(DisplayManager display);

    void update(float interval);

    void render(DisplayManager display);

    void onClose();

}
