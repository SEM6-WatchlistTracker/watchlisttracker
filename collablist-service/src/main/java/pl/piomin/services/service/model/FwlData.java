package pl.piomin.services.service.model;

import java.util.List;

public class FwlData {
    private List<FwlListModified> lists;
    private Integer hasRequests;

    public List<FwlListModified> getLists() {
        return lists;
    }
    public void setLists(List<FwlListModified> lists) {
        this.lists = lists;
    }
    
    public Integer getHasRequests() {
        return hasRequests;
    }
    public void setHasRequests(Integer hasRequests) {
        this.hasRequests = hasRequests;
    }

    public FwlData(List<FwlListModified> lists, Integer hasRequests) {
        this.lists = lists;
        this.hasRequests = hasRequests;
    }
}
