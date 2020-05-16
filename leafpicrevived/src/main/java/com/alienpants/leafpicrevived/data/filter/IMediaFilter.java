package com.alienpants.leafpicrevived.data.filter;

import com.alienpants.leafpicrevived.data.Media;

/**
 * Created by dnld on 4/10/17.
 */

public interface IMediaFilter {
    boolean accept(Media media);
}
