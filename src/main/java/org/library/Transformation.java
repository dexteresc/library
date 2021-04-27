package org.library;

/**
 * A lambda expression that transforms a value (commonly used as a method parameter).
 * @param <Input> The input type (e.g. ResultSet).
 * @param <Output> The output type (e.g. Account).
 * @see Database#query(String, Database.Configuration, Transformation) for a use-case example.
 * @see AccountRepository#getByID(int) for a usage example.
 */
public interface Transformation<Input, Output> {
    /**
     * Transforms the provided input value into the output type.
     * @param input A value of the input type.
     * @return A value of the output type that was created using the provided input value.
     * @throws Exception If an exception is thrown during the transformation.
     */
    Output materialize(Input input) throws Exception;
}