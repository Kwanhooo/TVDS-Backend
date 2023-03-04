package org.csu.tvds.core.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.csu.tvds.core.annotation.CoreIO;

@Data
@CoreIO
@AllArgsConstructor
@NoArgsConstructor
public class Output<T> {
    T data;
    boolean isSucceed;
}
