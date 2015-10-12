/**
 * 
 * @author jake mingolla
 *
 * @param <A> {@link Graph}
 * @param <B> {@link Graph}
 */
public abstract class Transition<A extends Graph, B extends Graph> implements Renderable {
	protected A base;
	protected B target;

    private int renderFrame = 0;
    private int totalRenderFrame = 100;
	
	Transition(A base, B target) {
		this.base = base;
		this.target = target;
	}
	
	public Class<A> getBaseClass() {
		return (Class) base.getClass();
	}
	
	public Class<B> getTargetClass() {
		return (Class) target.getClass();
	}

    public void setRenderFrame(int renderFrame) {
        this.renderFrame = renderFrame;
    }

    public void setTotalRenderFrame(int totalRenderFrame) {
        this.totalRenderFrame = totalRenderFrame;
    }

    public int getRenderFrame() {
        return renderFrame;
    }

    public int getTotalRenderFrame() {
        return totalRenderFrame;
    }

    public void resetRenderFrame() {
        renderFrame = 0;
    }

    public void tick() {
        renderFrame =+ 1;
    }

    public boolean isDone() {
        return renderFrame == totalRenderFrame;
    }
}
