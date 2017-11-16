package rest.utils;

public class MyThreadLocalTool<T> {
	private  ThreadLocal<T> tl = new ThreadLocal<T>(){
		@Override
		public void set(T value) {
			// TODO Auto-generated method stub
			super.set(value);
		}
		
		@Override
		public T get() {
			// TODO Auto-generated method stub
			return super.get();
		}
	};

	public ThreadLocal<T> getTl() {
		return tl;
	}

	public void setTl(ThreadLocal<T> tl) {
		this.tl = tl;
	}
	
	
}

	
