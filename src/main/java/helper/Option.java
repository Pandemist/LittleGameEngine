package helper;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class Option<T> {
	private final T value;

	private Option(T value) {
		this.value = value;
	}

	public static <T> Option<T> Some(T value) {
		return new Option<>(value);
	}

	public static <T> Option<T> None() {
		return new Option<>(null);
	}

	public boolean isSome() {
		return value != null;
	}

	public boolean isNone() {
		return value == null;
	}

	public T unwrap() {
		if (isSome()) {
			return value;
		} else {
			throw new NoSuchElementException("Option is None");
		}
	}

	public T unwrapOr(T defaultValue) {
		return isSome() ? value : defaultValue;
	}

	public T unwrapOrElse(Function<Void, T> supplier) {
		return isSome() ? value : supplier.apply(null);
	}

	public <R> Option<R> map(Function<T, R> mapper) {
		return isSome() ? Option.Some(mapper.apply(value)) : Option.None();
	}

	public void ifSome(Function<T, Void> consumer) {
		if (isSome()) {
			consumer.apply(value);
		}
	}

	public void ifNone(Runnable runnable) {
		if (isNone()) {
			runnable.run();
		}
	}

	public Optional<T> toOptional() {
		return Optional.ofNullable(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Option<?> option = (Option<?>) o;
		return Objects.equals(value, option.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return isSome() ? "Some(" + value + ")" : "None";
	}

}
