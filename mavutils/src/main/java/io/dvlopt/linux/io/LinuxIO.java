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


package io.dvlopt.linux.io ;


import com.sun.jna.Native     ;
import com.sun.jna.NativeLong ;
import com.sun.jna.Pointer    ;
import io.dvlopt.linux.SizeT  ;
import io.dvlopt.linux.SSizeT ;





/**
 * Class containing static methods and constants for performing basic native IO.
 * <P>
 * The API is kept close to what LibC offers so that users can easily write higher
 * level constructs.
 *
 * @see <a href="https://www.gnu.org/software/libc/manual/html_node/Low_002dLevel-I_002fO.html#Low_002dLevel-I_002fO">LibC low-level IO</a>
 */
public class LinuxIO {


    static {
    
        Native.register( "c" ) ;
    }




    // Flags for opening a stream.
    //
    public static final int O_APPEND    = 1024    ;
    public static final int O_ASYNC     = 8192    ;
    public static final int O_CLOEXEC   = 524288  ;
    public static final int O_CREAT     = 64      ;
    public static final int O_DIRECT    = 16384   ;
    public static final int O_DIRECTORY = 65536   ;
    public static final int O_DSYNC     = 4096    ;
    public static final int O_EXCL      = 128     ;
    public static final int O_LARGEFILE = 0       ;
    public static final int O_NOATIME   = 262144  ;
    public static final int O_NONBLOCK  = 2048    ;
    public static final int O_NOCTTY    = 256     ; 
    public static final int O_NOFOLLOW  = 131072  ;
    public static final int O_PATH      = 2097152 ;
    public static final int O_RDONLY    = 0       ;
    public static final int O_RDWR      = 2       ;
    public static final int O_SYNC      = 1052672 ;
    public static final int O_TMPFILE   = 4259840 ;
    public static final int O_TRUNC     = 512     ;
    public static final int O_WRONLY    = 1       ;


    // Flags for setting permissions on a resource.
    //
    public static final int S_IRWXU = 448 ;
    public static final int S_IRUSR = 256 ;
    public static final int S_IWUSR = 128 ;
    public static final int S_IXUSR = 64  ;
    public static final int S_IRWXG = 56  ;
    public static final int S_IRGRP = 32  ;
    public static final int S_IWGRP = 16  ;
    public static final int S_IXGRP = 8   ;
    public static final int S_IRWXO = 7   ;
    public static final int S_IROTH = 4   ;
    public static final int S_IWOTH = 2   ;
    public static final int S_IXOTH = 1   ;


    // Values for seeking a stream.
    //
    public static final int SEEK_SET = 0 ;
    public static final int SEEK_CUR = 1 ;
    public static final int SEEK_END = 2 ;




    // Privatizes constructor as this class provides only static methods.
    //
    private LinuxIO() {}




    // 
    // Public natives functions.
    //


    public static native int close( int fd ) ;


    public static native int ioctl( int        fd      ,
                                    NativeLong request ,
                                    int        arg     ) ;


    public static native int ioctl( int        fd      ,
                                    NativeLong request ,
                                    long       arg     ) ;
                                    

    public static native int ioctl( int        fd ,
                                    NativeLong request ,
                                    NativeLong arg     ) ;


    public static native int ioctl( int        fd      ,
                                    NativeLong request ,
                                    Pointer    arg     ) ;


    public static native long lseek64( int  fd      ,
                                       long offset  ,
                                       int  whence  ) ;


    public static native int open64( String path  ,
                                     int    flags ) ;


    public static native int open64( String path  ,
                                     int    flags ,
                                     int    mode  ) ;


    public static native int openat64( int    dirFD ,
                                       String path  ,
                                       int    flags ) ;


    public static native int openat64( int    dirFD ,
                                       String path  ,
                                       int    flags ,
                                       int    mode  ) ;


    public static native SSizeT read( int    fd     ,
                                      byte[] buffer ,
                                      SizeT  size   ) ;


    public static native SSizeT read( int     fd     ,
                                      Pointer buffer ,
                                      SizeT   size   ) ;


    public static native SSizeT write( int    fd     ,
                                       byte[] buffer ,
                                       SizeT  size   ) ;


    public static native SSizeT write( int     fd     ,
                                       Pointer buffer ,
                                       SizeT   size   ) ;
}
