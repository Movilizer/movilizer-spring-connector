package com.movilizer.mds.connector.utils;

import com.movilitas.movilizer.v15.*;
import com.movilizer.mds.connector.model.MovilizerRequestSink;


public interface MovilizerMoveletUtils {

    void createMovelet(MovilizerMovelet movelet, MovilizerRequestSink sink);

    void assignMoveletToParticipant(String moveletKey, MovilizerParticipant participant,
                                           MovilizerRequestSink sink);

    void assignMoveletToParticipant(String moveletKey, String moveletKeyExtension,
                                           MovilizerParticipant participant,
                                    MovilizerRequestSink sink);

    void removeMovelet(String moveletKey, String moveletKeyExtension, Boolean ignoreExtensionKey,
                       MovilizerRequestSink sink);

}
