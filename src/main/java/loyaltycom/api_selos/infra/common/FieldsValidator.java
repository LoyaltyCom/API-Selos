package loyaltycom.api_selos.infra.common;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class FieldsValidator {
    public void validateFields(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<String> errorMessages = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.getName().equalsIgnoreCase("id")) {
                if (!field.getName().equalsIgnoreCase("criadoEm")) {
                    try {
                        Object value = field.get(obj);

                        if (value == null) {
                            errorMessages.add("Campo '" + field.getName() + "' é obrigatório.");
                        } else if (value instanceof String && ((String) value).trim().isEmpty()) {
                            errorMessages.add("Campo '" + field.getName() + "' não pode estar vazio.");
                        }

                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Erro ao acessar o campo: " + field.getName());
                    }
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new IllegalArgumentException(String.join(" | ", errorMessages));
        }
    }
}
