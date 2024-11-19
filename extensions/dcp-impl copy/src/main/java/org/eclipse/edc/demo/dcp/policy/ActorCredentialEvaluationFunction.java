package org.eclipse.edc.demo.dcp.policy;

//import org.eclipse.edc.participant.spi.ParticipantAgentPolicyContext;
import org.eclipse.edc.policy.engine.spi.AtomicConstraintRuleFunction;
import org.eclipse.edc.policy.engine.spi.PolicyContext;
import org.eclipse.edc.policy.model.Operator;
import org.eclipse.edc.policy.model.Permission;

public class ActorCredentialEvaluationFunction<C extends PolicyContext> extends AbstractCredentialEvaluationFunction implements AtomicConstraintRuleFunction<Permission, C> {

    private static final String ACTOR_CREDENTIAL_NAMESPACE = "https://w3id.org/actor/credentials/";
    private static final String ACTOR_CONSTRAIN_KEY = "ActorCredential";
    private static final String ACTOR_CREDENTIAL_TYPE = "ActorCredential";
    private static final String ACTOR_TYPE_CLAIM = "actorType";
    private static final String ACTOR_ID_CLAIM = "actorId";
    private static final String ACTOR_NAME_CLAIM = "actorName";

    private ActorCredentialEvaluationFunction() {
    }

    public static <C extends PolicyContext> ActorCredentialEvaluationFunction<C> create() {
        return new ActorCredentialEvaluationFunction<>() {
        };
    }

    @Override
    public boolean evaluate(Operator operator, Object rightValue, Permission rule, C context) {
        System.out.println("ActorCredentialEvaluationFunction.evaluate");
        System.out.println("Context: " + context);

        /*if (!operator.equals(Operator.EQ)) {
            context.reportProblem("Invalid operator '%s', only accepts '%s'".formatted(operator, Operator.EQ));
            return false;
        }

        var pa = context.participantAgent();
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
                ); */
        return true;
    }
}