package com.pixogram.users.payload.request;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Anushkha Thakur
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MultipleMediaRequest {
    private List<SingleMediaRequest> mediaList;
}
