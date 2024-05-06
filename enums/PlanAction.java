package ng.optisoft.rosabon.enums;

public class PlanAction {
    public enum EntityPlanAction {
        TOP_UP, TRANSFER, WITHDRAW, AUTO_ROLLOVER, HISTORY,
        VIEW_ACCOUNT_DETAIL, PAY_WITH_CARD, REMOVE,
        ROLLOVER, NEW_INVESTMENT
    }

    public enum EntityActivePlanAction

    {
        TOP_UP, TRANSFER, WITHDRAW,
                AUTO_ROLLOVER, HISTORY,

    }

    public enum EntityPendingPlanAction {
        VIEW_ACCOUNT_DETAIL, PAY_WITH_CARD,
        REMOVE
    }

   public enum EntityMaturedPlanAction {
        ROLLOVER, TRANSFER, WITHDRAW, HISTORY
    }

}