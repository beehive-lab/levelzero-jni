/*
 * MIT License
 *
 * Copyright (c) 2024, APT Group, Department of Computer Science,
 * The University of Manchester.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package uk.ac.manchester.tornado.drivers.spirv.levelzero;

public class Pointer<T> {

    private final T value;

    private Pointer(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public static Pointer<Integer> to(int value) {
        return new Pointer<>(value);
    }

    public static Pointer<Long> to(long value) {
        return new Pointer<>(value);
    }

    public static Pointer<Short> to(short value) {
        return new Pointer<>(value);
    }

    public static Pointer<Byte> to(byte value) {
        return new Pointer<>(value);
    }

    public static Pointer<Character> to(char value) {
        return new Pointer<>(value);
    }

    public static Pointer<Float> to(float value) {
        return new Pointer<>(value);
    }

    public static Pointer<Double> to(double value) {
        return new Pointer<>(value);
    }

    public static Pointer<Boolean> to(boolean value) {
        return new Pointer<>(value);
    }
}
