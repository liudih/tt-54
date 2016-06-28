package codec;

public interface ICodec<F, T> {

	Class<? extends F> getSourceClass();

	Class<? extends T> getTargetClass();

	T encode(F fromObj);

	F decode(T fromObj);

}
