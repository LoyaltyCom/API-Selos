package loyaltycom.api_selos.infra.customers_routing_config;

public class ClientContextHolder {
    private static final ThreadLocal<String> currentDatabase = new ThreadLocal<>();

    public static void setCurrentDatabase(String database) {
        currentDatabase.set(database);
    }

    public static String getCurrentDatabase() {
        return currentDatabase.get();
    }

    public static void clear() {
        currentDatabase.remove();
    }
}


