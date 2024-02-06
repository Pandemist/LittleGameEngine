package helper;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Result<T, E extends Throwable> {
	private final T value;
	private final E error;

	private Result(T value, E error) {
		this.value = value;
		this.error = error;
	}

	public static <T, E extends Throwable> Result<T, E> ok(T value) {
		return new Result<>(value, null);
	}

	public static <T, E extends Throwable> Result<T, E> err(E error) {
		return new Result<>(null, error);
	}

	public boolean isOk() {
		return value != null;
	}

	public boolean isErr() {
		return error != null;
	}

	public T unwrap() throws E {
		if (isOk()) {
			return value;
		} else {
			throw error;
		}
	}

	public T unwrapOr(T defaultValue) {
		return isOk() ? value : defaultValue;
	}

	public T unwrapOrElse(Function<E, T> supplier) {
		return isOk() ? value : supplier.apply(error);
	}

	public <R> Result<R, E> map(Function<T, R> mapper) {
		return isOk() ? Result.ok(mapper.apply(value)) : Result.err(error);
	}

	public <F extends Throwable> Result<T, F> mapErr(Function<E, F> mapper) {
		return isOk() ? Result.ok(value) : Result.err(mapper.apply(error));
	}

	public Optional<T> toOptional() {
		return Optional.ofNullable(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Result<?, ?> result = (Result<?, ?>) o;
		return Objects.equals(value, result.value) &&
				Objects.equals(error, result.error);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, error);
	}

	@Override
	public String toString() {
		return isOk() ? "Ok(" + value + ")" : "Err(" + error + ")";
	}
}
