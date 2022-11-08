package com.yeet.metalib;

@SuppressWarnings("all")
public class MetadataStructure {
    public Rom rom;

    public enum MachineType {
        Z2, Z3, Z5, Z6;
    }

    public enum PackageType {
        system, boot, misc;
    }

    public class Rom {
        public RomItem latest;
        public RomItem[] history;
    }

    public class RomItem {
        public String version;
        public String packageUri;
        public String changeLogUri;
        public String lastUpgradableVersion;
        public MachineType[] nonApplicableMachineType;
        public String md5;
    }

    public class BannedVersion {
        public String version;
        public String reason;
    }
}
