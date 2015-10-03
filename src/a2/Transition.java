package a2;

public abstract class Transition<A extends Graph, B extends Graph> implements Renderable {
	protected A base;
	protected B target;
	
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
	
	public void transition() {
	}
}
