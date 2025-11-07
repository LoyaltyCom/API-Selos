package loyaltycom.api_selos.infra.common;

import org.springframework.stereotype.Service;

@Service
public class StringUtils {
    public String capitalizeFirstLetter(String input){
        if (input == null || input.isEmpty()){

            return null;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
