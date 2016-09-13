package drwho.gallifreyan.application;

import drwho.gallifreyan.visual.Plan;

/**
 * Actually create something with the plan, like a picture or a web-page
 */
public interface Renderer {

    /**
     * Draw the Gallifreyan that the plan tells you to
     * @param p    the plan to visualise
     */
    void render(Plan p);

}
