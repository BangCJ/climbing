package com.bang.ap.encrypt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventVO {

    private String eventSourceDomainCode;
    private String eventDomainName;
    private String domainMaster;
    private String eventDomainApplicationCode;
    private String eventDomainApplicationName;
    private String charge;
    private String applicationUser;
    private String eventSourceCode;
    private String eventSourceName;
    private String eventTypeCode;
    private String eventTypeName;

    private String eventListenerDomainCode;
    private String eventListenerDomainName;
    private String eventListenerApplicationCode;
    private String eventListenerApplicationName;
    private String eventListenerApplicationUser;
    private String eventListenerDomainMaster;
    private String tps;
    private int limit;
    private String ext;
    private String nodeCode;
    private String eventListenerName;
    private String eventListenerInterface;





}
