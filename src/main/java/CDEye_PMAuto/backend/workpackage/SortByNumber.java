package CDEye_PMAuto.backend.workpackage;

import java.util.Comparator;

public class SortByNumber implements Comparator<WorkPackage>
{
    public int compare(WorkPackage a, WorkPackage b)
    {
        return Integer.parseInt(b.getWorkPackageNumber())
                - Integer.parseInt(a.getWorkPackageNumber());
    }
}
