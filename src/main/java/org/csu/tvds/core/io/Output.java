package org.csu.tvds.core.io;

import lombok.Data;

@Data
public class Output<T> {
    T data;
    boolean isSucceed;
}
