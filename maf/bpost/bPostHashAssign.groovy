class GV {
    static Long systemId;
    static def notify;
    static def attributeManager;
    static def mds;
    static String deviceAddress;
    static String hashAttributeValue;
}

class GlobalVars {
    static final String attributeGroup = "MOVILIZER_ASSIGNMENT_BOARD_ADDITIONAL";
    static final String attributeGroupToDelete = "MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY";
    static final String hashAttributeField = "HASH";

    static final String moveletKey = "com.movilizer.bpost.hashTrigger";
    static final String moveletKeyExtension = "";
    static final String mail = "roberto.demiguel@movilizer.com";
}

// To execute
main();

def main() {
    long initialTime = new Date().time;

    //init mafContext variables
    init();
    //get all active participants (with MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY group)
    List<MAFParticipantKey> allParticipantKeys = getParticipants(GlobalVars.attributeGroupToDelete);
    //get participants that haven't got same hash as received
    List<MAFParticipantKey> participantsWithoutHash = getParticipantsWithoutHash(allParticipantKeys, GV.hashAttributeValue);
    //remove MOVILIZER_ASSIGNMENT_BOARD_HIERARCHY group to deactivate participants without hash updated
    removeAttributeFromFirst100ParticipantList(GlobalVars.attributeGroupToDelete, participantsWithoutHash);

    logMD("info", "allParticipantKeys.size()", " before triggering again: " + allParticipantKeys.size(), "");
    logMD("info", "participantsWithoutHash.size()", " before triggering again: " + participantsWithoutHash.size(), "");

    if (participantsWithoutHash.size() > 100) {
        logMD("info", "triggered", "OK" , "");
//trigger again this script
        triggerScript();
    } else {
        logMD("info", "NOT triggered", "finished" , "");
    }

    long finalTime = new Date().time;
    String executionTimeInSeconds = (finalTime - initialTime) / 1000.0 + "";
    logMD("info", "main()", "hashBpost:[" + executionTimeInSeconds + "]Seconds", "");
}


def init() {
    GV.systemId = mafContext.getSystemID().toLong();
    GV.notify = mafContext.getNotificationManager();
    GV.attributeManager = mafContext.getAttributeManager();
    GV.mds = mafContext.getMDSLifecycleManager();
    GV.deviceAddress = mafContext.getDeviceAddress();
    //init received hash value
    if(GV.deviceAddress.split("@").length > 1){
        GV.hashAttributeValue = GV.deviceAddress.split("@")[1];
    }
}


List<MAFParticipantKey> getParticipants(String attributeGroup) {
    MAFAttributeSearch matchingAttributes = new MAFAttributeSearch(GV.systemId, attributeGroup);
    List<MAFParticipantKey> participantKeys = GV.attributeManager.getParticipantKeys(matchingAttributes)
//    logMD("info", "getParticipants() of: " + attributeGroup, "success-size: " + participantKeys.size(), "");
    return participantKeys;
}


List<MAFParticipantKey> getParticipantsWithoutHash(List<MAFParticipantKey> allParticipantKeys, String hash) {
    List<MAFParticipantKey> participantsWithoutHash = new ArrayList<MAFParticipantKey>();
    for (i = 0; i < allParticipantKeys.size(); i++) {
        boolean updated = hasUpdatedHashValue(allParticipantKeys.get(i), hash);
        if (!updated) {
            participantsWithoutHash.add(allParticipantKeys.get(i));
        }
    }
//    logMD("info", "getParticipantsWithoutHash() hash: " + hash, "success-size: " + participantsWithoutHash.size(), "");
    return participantsWithoutHash;
}

boolean hasUpdatedHashValue(MAFParticipantKey participantKey, String hash) {
    boolean updatedHash = false;
    List<MAFAttributeHierarchy> participantAttributes = GV.attributeManager.getAttributes(participantKey);

    for (MAFAttributeHierarchy groupAttribute in participantAttributes) {

        String group = groupAttribute.getGroup();
        if (group.equals(GlobalVars.attributeGroup)) {
            String participantHash = getHashValueFromGroup(groupAttribute);
//            logMD("info", "participantHash(): " + participantHash, "- hash: " + hash, "");
            if ((participantHash != null) && participantHash.equals(hash)) {
                updatedHash = true;
            }
            break;
        }
    }
    return updatedHash;
}

String getHashValueFromGroup(MAFAttributeHierarchy attributeHierarchy) {
    String hash = null;
    for (level in attributeHierarchy.getLevels()) {
        if (level.getName().equals(GlobalVars.hashAttributeField)) {
            hash = level.getValue();
            break;
        }
    }
    return hash;
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
    String participantKey = String.valueOf(systemTime);
    String participantName = String.valueOf(systemTime);
    String queue = null;

    GV.mds.assignMovelet(GV.systemId, GlobalVars.moveletKey, GlobalVars.moveletKeyExtension, GV.deviceAddress, participantKey, participantName, queue);
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

def logMail(logMessage) {
    GV.notify.sendMailNotification(GlobalVars.mail, 'Log message', 'Log message: ' + logMessage);
}


