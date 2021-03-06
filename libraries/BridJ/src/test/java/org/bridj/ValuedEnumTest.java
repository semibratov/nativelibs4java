/*
 * BridJ - Dynamic and blazing-fast native interop for Java.
 * http://bridj.googlecode.com/
 *
 * Copyright (c) 2010-2013, Olivier Chafik (http://ochafik.com/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Olivier Chafik nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY OLIVIER CHAFIK AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.bridj;

import java.util.Collections;
import java.util.Iterator;
import org.bridj.ann.*;
import org.junit.Test;
import static org.junit.Assert.*;

@Library("test")
public class ValuedEnumTest {
    static {
		BridJ.register();
	}
	public enum MyEnum implements IntValuedEnum<MyEnum > {
		One(0),
		Two(1),
		Three(2);
		MyEnum(long value) {
			this.value = value;
		}
		public final long value;
		public long value() {
			return this.value;
		}
		public Iterator<MyEnum > iterator() {
			return Collections.singleton(this).iterator();
		}
		public static ValuedEnum<MyEnum > fromValue(long value) {
			return FlagSet.fromValue(value, values());
		}
	};
	public static native ValuedEnum<MyEnum > intToMyEnum(int value);
	public static native int MyEnumToInt(ValuedEnum<MyEnum > value);
    
    private static final int nTests = 100000;
    
    @Test
    public void testSingleIntToEnum() {
        MyEnum expected = MyEnum.Two;
        int expectedInt = (int)expected.value();
        ValuedEnum<MyEnum> ret = intToMyEnum(expectedInt);
        FlagSet<MyEnum> f = (FlagSet<MyEnum>)ret;
        assertEquals(MyEnum.class, f.getEnumClass());
        assertEquals(expectedInt, f.value());
    }
    
    @Test
    public void testIntToEnum() {
        MyEnum expected = MyEnum.Two;
        int expectedInt = (int)expected.value();
        for (int i = 0; i < nTests; i++) {
            ValuedEnum<MyEnum> ret = intToMyEnum(expectedInt);
            if (expectedInt != ret.value())
                assertEquals(expectedInt, ret.value());
        }
    }
    
    
    @Test
    public void testEnumToInt() {
        MyEnum expected = MyEnum.Two;
        int expectedInt = (int)expected.value();
        for (int i = 0; i < nTests; i++) {
            int ret = MyEnumToInt(expected);
            if (expectedInt != ret)
                assertEquals(expectedInt, ret);
        }
    }
}
