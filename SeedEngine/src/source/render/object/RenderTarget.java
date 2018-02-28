package source.render.object;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import source.render.DisplayManager;

/**
 * A framebuffer object with enclosed texture channels
 *
 * @author Zach Harris
 * @version 0.1
 * @since Version 0.1
 */
public class RenderTarget {
    private final int buffer;
    private final int[] textures;
    private final int depthBuffer;

    /**
     * Constructs a RenderTarget with one bound texture
     */
    public RenderTarget() {
        this(1);
    }

    /**
     * Constructs a RenderTarget with a set number of textures
     * @param textureCount number of textures
     */
    public RenderTarget(int textureCount) {
        this.buffer = GL30.glGenFramebuffers();
        int[] textures = new int[textureCount];
        for (int i = 0; i < textureCount; i++) {
            textures[i] = GL11.glGenTextures();
        }
        this.textures = textures;
        this.depthBuffer = GL30.glGenRenderbuffers();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);

        for (int i = 0; i < textureCount; i++) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i]);
            GL11.glTexImage2D(
                    GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, DisplayManager.getWidth(),
                    DisplayManager.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0
            );
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT,
                DisplayManager.getWidth(), DisplayManager.getHeight());

        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

    }

    /**
     * Binds all relevant data to the graphics card
     */
    public void attachToRenderer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, buffer);

        int[] attachments = new int[textures.length];
        for (int i = 0; i < textures.length; i++) {
            attachments[i] = GL30.GL_COLOR_ATTACHMENT0 + i;
        }

        GL20.glDrawBuffers(attachments);

        for (int i = 0; i < textures.length; i++) {
            GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + i,
                    GL11.GL_TEXTURE_2D, textures[i], 0);
        }

        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
                depthBuffer);

    }

    /**
     * Gets the associated buffer id
     * @return buffer id
     */
    public int getBuffer() {
        return buffer;
    }

    /**
     * Returns the texture ID in slot i
     * @param i slot to use
     * @return texture ID
     */
    public int getTextureSlot(int i) {
        return this.textures[i];
    }

    /**
     * Binds all textures to the graphics card
     * @return texture slots used
     */
    public int bindAllTextures() {
        for (int i = 0; i < textures.length; i++) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[i]);
        }

        return textures.length;
    }

    public void cleanUp() {
        GL30.glDeleteFramebuffers(buffer);
        for (int id : textures) {
            GL11.glDeleteTextures(id);
        }
        GL30.glDeleteRenderbuffers(depthBuffer);
    }

}
