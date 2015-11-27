class GV {
    static Long systemId;
    static def notify;
    static def attributeManager;
    static def mds;
    static String deviceAddress;
}

class GlobalVars {
    static final String attributeGroupToDelete = "MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY";
    static final String moveletKey = "com.movilizer.bpost.deleteAllTrigger";
    static final String moveletKeyExtension = "";
}

// To execute
main();

def main() {
    long initialTime = new Date().time;
    //init mafContext variables
    init();
    //get all active participants (with MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY group)
    List<MAFParticipantKey> allParticipantKeys = getParticipantsInHierarchy(GlobalVars.attributeGroupToDelete);
    //remove MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY group to deactivate participants without hash updated
    removeAttributeFromFirst100ParticipantList(GlobalVars.attributeGroupToDelete, allParticipantKeys);
    logMD("info", "allParticipantsKeys.size()", " before triggering again: " + allParticipantKeys.size() - 100, "");
    if (allParticipantKeys.size() > 100) {
        //trigger again this script
        logMD("info", "triggered", "OK" , "");
        triggerScript();
    } else {
        logMD("info", "NOT triggered", "finished" , "");
    }
    long finalTime = new Date().time;
    String executionTimeInSeconds = (finalTime - initialTime) / 1000.0 + "";
    logMD("info", "main()", "deleteAll:[" + executionTimeInSeconds + "]Seconds", "");
}

def init() {
    GV.systemId = mafContext.getSystemID().toLong();
    GV.notify = mafContext.getNotificationManager();
    GV.attributeManager = mafContext.getAttributeManager();
    GV.mds = mafContext.getMDSLifecycleManager();
    GV.deviceAddress = mafContext.getDeviceAddress();
}

List<MAFParticipantKey> getParticipantsInHierarchy(String attributeGroup) {
    MAFAttributeSearch matchingAttributes = new MAFAttributeSearch(GV.systemId, attributeGroup);
    List<MAFParticipantKey> participantKeys = GV.attributeManager.getParticipantKeys(matchingAttributes)
//    logMD("info", "getParticipantsInHierarchy() of: " + attributeGroup, "success-size: " + participantKeys.size(), "");
    return participantKeys;
}

def removeAttributeFromFirst100ParticipantList(String attributeGroup, List<MAFParticipantKey> participantsWithoutHash) {
    int i = 0;
    while ((i < 100) && (i < participantsWithoutHash.size())) {
        MAFAttributeSearch matchingAttributes = new MAFAttributeSearch(GV.systemId, attributeGroup);
        GV.attributeManager.deleteAttribute(participantsWithoutHash.get(i), matchingAttributes);
        i++;
    }
//    logMD("info", "removeAttributeFromFirst100ParticipantList()", "success", "");
}

def triggerScript() {
    long systemTime = new Date().time;
    String deviceAddress = "@" + String.valueOf(systemTime) + "_bpost_test@movilizer.com";
    String participantKey = String.valueOf(systemTime);
    String participantName = String.valueOf(systemTime);
    String queue = null;

    GV.mds.assignMovelet(GV.systemId, GlobalVars.moveletKey, GlobalVars.moveletKeyExtension, deviceAddress, participantKey, participantName, queue);
}

/////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Helper for loging with masterdata
 */
def logMD(String type, String method, String message, String data) {
    Hashtable<String, String> logTable = new Hashtable<String, String>();
    logTable.putAt("log-data", data);
    GV.mds.sendMasterdata(GV.systemId, "log." + type, method, message, null, null, null, null, null, null, null, 'default', logTable);
}



