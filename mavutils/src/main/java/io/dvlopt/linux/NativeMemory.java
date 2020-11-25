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


import com.sun.jna.Memory    ;
import com.sun.jna.Native    ;
import com.sun.jna.Pointer   ;
import io.dvlopt.linux.SizeT ;




/**
 * Class providing static methods for doing operations on JNA pointers and managed memory.
 */
public class NativeMemory {


    static {
    
        Native.register( "c" ) ;
    }



    private static native int memcmp( Pointer ptr1 ,
                                      Pointer ptr2 ,
                                      SizeT   n    ) ;


    private static native void memcpy( Pointer ptrOrigin ,
                                       Pointer ptrTarget ,
                                       SizeT   n         ) ;


    private static native void memset( Pointer ptr ,
                                       int     b   ,
                                       SizeT   n   ) ;




    // Privatizes constructor.
    //
    private NativeMemory() {} ;




    /**
     * Copies `<strong>n</strong>` bytes from a pointer to another.
     *
     * @param ptrOrigin  Pointer holding to bytes to copy.
     *
     * @param ptrTarget  Pointer where the bytes will be copied.
     *
     * @param n  How many bytes will be copied.
     */
    public static void copy( Pointer ptrOrigin ,
                             Pointer ptrTarget ,
                             SizeT   n         ) {
    
        memcpy( ptrOrigin ,
                ptrTarget ,
                n         ) ;
    }




    /**
     * Copies all the bytes of a managed pointer to another one.
     *
     * @param ptrOrigin  Managed pointer whose bytes will be copied.
     *
     * @param ptrTarget  Pointer where the bytes will be copied.
     */
    public static void copy( Memory  ptrOrigin ,
                             Pointer ptrTarget ) {
    
        memcpy( ptrOrigin                     ,
                ptrTarget                     ,
                new SizeT( ptrOrigin.size() ) ) ;
    }




    /**
     * Tests two pointers for equality, byte per byte.
     *
     * @param ptr1  First pointer.
     *
     * @param ptr2  Second pointer.
     *
     * @param n  How many bytes will be tested.
     *
     * @return  True if all tested bytes are equal.
     */
    public static boolean equal( Pointer ptr1 ,
                                 Pointer ptr2 ,
                                 SizeT   n    ) {
    
        return memcmp( ptr1 ,
                       ptr2 ,
                       n    ) == 0 ;
    }




    /**
     * Tests a managed pointer against a regular one for equality.
     *
     * @param ptr1  Managed pointer.
     *
     * @param ptr2  Regular pointer.
     *
     * @return  True if all tested bytes are equal.
     */
    public static boolean equal( Memory  ptr1 ,
                                 Pointer ptr2 ) {
    
        return equal( ptr1                     ,
                      ptr2                     ,
                      new SizeT( ptr1.size() ) ) ;
    }




    /**
     * Tests a regular pointer against a managed one for equality.
     *
     * @param ptr1  Regular pointer.
     *
     * @param ptr2  Managed pointer.
     *
     * @return  True if all tested bytes are equal.
     */
    public static boolean equal( Pointer ptr1 ,
                                 Memory  ptr2 ) {
    
        return equal( ptr1                     ,
                      ptr2                     ,
                      new SizeT( ptr2.size() ) ) ;
    }




    /**
     * Tests two managed pointers for equality.
     *
     * @param ptr1  First managed pointer.
     *
     * @param ptr2  Second managed pointer.
     *
     * @return  True if all tested bytes are equal.
     */
    public static boolean equal( Memory ptr1 ,
                                 Memory ptr2 ) {
    
        return equal( ptr1          ,
                      (Pointer)ptr2 ) ;
    }




    /**
     * Given a pointer, sets `<strong>n</strong>` bytes to value `<strong>b</strong>`.
     *
     * @param ptr  Pointer acting as a starting point.
     *
     * @param b  Unsigned byte.
     *
     * @param n  How many bytes.
     */
    public static void fill( Pointer ptr ,
                             int     b   ,
                             SizeT   n   ) {
    
        memset( ptr ,
                b   ,
                n   ) ;
    }




    /**
     * Given a managed pointer, sets all bytes to value `<strong>b</strong>`.
     *
     * @param ptr  Managed pointer.
     *
     * @param b  Unsigned byte.
     */
    public static void fill( Memory ptr ,
                             int    b   ) {
    
        fill( ptr                     ,
              b                       ,
              new SizeT( ptr.size() ) ) ;
    }




    /**
     * Given a pointer, reads an unsigned byte value.
     *
     * @param ptr  Pointer.
     *
     * @param offset  Byte offset from the pointer.
     *
     * @return  Unsigned byte.
     */
    public static int getUnsignedByte( Pointer ptr    ,
                                       long    offset ) {
    
        return   ptr.getByte( offset )
               & 0x000000ff            ;
    }




    /**
     * Given a pointer, writes an unsigned byte value.
     *
     * @param ptr  Pointer.
     *
     * @param offset  Byte offset from the pointer.
     *
     * @param b  Unsigned byte.
     */
    public static void setUnsignedByte( Pointer ptr    ,
                                        long    offset ,
                                        int     b      ) {
    
        ptr.setByte( offset  ,
                     (byte)b ) ;
    }




    /**
     * Given a pointer, reads an unsigned short value.
     *
     * @param ptr  Pointer.
     *
     * @param offset  Byte offset from the pointer.
     *
     * @return  Unsigned short.
     */
    public static int getUnsignedShort( Pointer ptr    ,
                                        long    offset ) {
    
        return   ptr.getShort( offset )
               & 0x0000ffff             ;
    }




    /**
     * Given a pointer, writes an unsigned short value.
     *
     * @param ptr  Pointer.
     *
     * @param offset  Byte offset from the pointer.
     *
     * @param s  Unsigned short.
     */
    public static void setUnsignedShort( Pointer ptr    ,
                                         long    offset ,
                                         int     s      ) {
    
        ptr.setShort( offset    ,
                      (short) s ) ;
    }




    /**
     * Given a pointer, reads an unsigned int value.
     *
     * @param ptr  Pointer.
     *
     * @param offset  Byte offset from the pointer.
     *
     * @return  Unsigned int.
     */
    public static long getUnsignedInt( Pointer ptr    ,
                                       long    offset ) {
    
        return   ptr.getInt( offset )
               & 0xffffffffL          ; 
    }




    /**
     * Given a pointer, writes an unsigned int value.
     *
     * @param ptr  Pointer.
     *
     * @param offset  Byte offset from the pointer.
     *
     * @param i Unsigned int.
     */
    public static void setUnsignedInt( Pointer ptr    ,
                                       long    offset ,
                                       long    i      ) {
    
        ptr.setInt( offset ,
                    (int)i ) ;
    }
}
