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

#include "levelZeroPointer.h"

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (B)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__B
  (JNIEnv * env, jclass klass, jbyte value) {
  return jlong(&value);
}

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__J
 (JNIEnv * env, jclass klass, jlong value) {
  return jlong(&value);
}
/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (S)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__S
 (JNIEnv * env, jclass klass, jshort value) {
  return jlong(&value);
}

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__I
 (JNIEnv * env, jclass klass, jint value) {
 return jlong(&value);
}

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (F)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__F
 (JNIEnv * env, jclass klass, jfloat value) {
  return jlong(&value);
}

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (D)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__D
 (JNIEnv * env, jclass klass, jdouble value) {
  return jlong(&value);
}

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (C)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__C
 (JNIEnv * env, jclass klass, jchar value) {
  return jlong(&value);
}

/*
 * Class:     uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer
 * Method:    to
 * Signature: (Z)J
 */
JNIEXPORT jlong JNICALL Java_uk_ac_manchester_tornado_drivers_spirv_levelzero_Pointer_to__Z
 (JNIEnv * env, jclass klass, jboolean value) {
  return jlong(&value);
}