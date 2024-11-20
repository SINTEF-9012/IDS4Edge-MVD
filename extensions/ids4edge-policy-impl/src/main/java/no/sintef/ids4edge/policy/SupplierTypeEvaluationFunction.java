package no.sintef.ids4edge.policy;

import org.eclipse.edc.policy.engine.spi.AtomicConstraintRuleFunction;
import org.eclipse.edc.policy.engine.spi.PolicyContext;
import org.eclipse.edc.policy.model.Duty;
import org.eclipse.edc.policy.model.Operator;

public class SupplierTypeEvaluationFunction<C extends PolicyContext> extends AbstractCredentialEvaluationFunction implements AtomicConstraintRuleFunction<Duty, C> {
    public static final String SUPPLIER_TYPE_CONSTRAIN_KEY = "SupplierType";

    private SupplierTypeEvaluationFunction() {
    }

    public static <C extends PolicyContext> SupplierTypeEvaluationFunction<C> create() {
        return new SupplierTypeEvaluationFunction<>() {
        };
    }

    @Override
    public boolean evaluate(Operator operator, Object rightValue, Duty duty, C context) {
        System.out.println("SupplierTypeEvaluationFunction.evaluate - rightValue: " + rightValue);
        System.out.println("Context: " + context);

        if (!operator.equals(Operator.EQ)) {
            context.reportProblem("Invalid operator '%s', only accepts '%s'".formatted(operator, Operator.EQ));
            return false;
        }

        return true;

        /*var pa = context.participantAgent();
        if (pa == null) {
            context.reportProblem("No ParticipantAgent found on context.");
            return false;
        }

        var credentialResult = getCredentialList(pa);
        if (credentialResult.failed()) {
            context.reportProblem(credentialResult.getFailureDetail());
            return false;
        }

        return credentialResult.getContent()
                .stream()
                .filter(vc -> vc.getType().stream().anyMatch(t -> t.endsWith(ACTOR_CREDENTIAL_TYPE)))
                .flatMap(credential -> credential.getCredentialSubject().stream())
                .anyMatch(
                        credentialSubject -> {
                            var actorType = credentialSubject.getClaim(ACTOR_CREDENTIAL_NAMESPACE, ACTOR_TYPE_CLAIM);
                            return actorType != null &&
                                    actorType.equals(rightValue);
                        }
                );*/
    }
}
