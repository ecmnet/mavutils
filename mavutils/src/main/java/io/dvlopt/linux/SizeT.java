/*
 * Copyright 2018 Adam Helinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.dvlopt.linux ;


import com.sun.jna.IntegerType ;
import com.sun.jna.Native      ;




/**
 * Class representing the native <code>size_t</code> type which is architecture-specific.
 * <p>
 * It is an unsigned integer typically representing the length of something.
 */
public class SizeT extends IntegerType {


    /**
     * In bytes, the size of <code>size_t</code>.
     */
    public static final int SIZE = Native.SIZE_T_SIZE ;




    /**
     * Constructor defaulting to 0.
     */
    public SizeT() {
    
        this( 0 ) ;
    }




    /**
     * Constructor with a value.
     *
     * @param value  The original value, cannot be larger than <code>size_t</code>.
     */
    public SizeT( long value ) {
    
        super( SIZE  ,
               value ,
               true  ) ;
    }
}
