package org.eclipse.edc.demo.dcp.core;

import org.eclipse.edc.iam.identitytrust.spi.scope.ScopeExtractor;
import org.eclipse.edc.policy.context.request.spi.RequestPolicyContext;
import org.eclipse.edc.policy.model.Operator;

import java.util.Set;

public class ActorScopeExtractor implements ScopeExtractor {
    private static final String ACTOR_CREDENTIAL_TYPE = "ActorCredential";
    private static final String ACTOR_SCOPE_PREFIX = "ActorCredential.";
    private static final String CREDENTIAL_TYPE_NAMESPACE = "org.eclipse.edc.vc.type";

    @Override
    public Set<String> extractScopes(Object leftValue, Operator operator, Object rightValue, RequestPolicyContext context) {
        Set<String> scopes = Set.of();
        if (leftValue instanceof String leftOperand) {
            if (leftOperand.startsWith(ACTOR_SCOPE_PREFIX)) {
                scopes = Set.of("%s:%s:read".formatted(CREDENTIAL_TYPE_NAMESPACE, ACTOR_CREDENTIAL_TYPE));
            }
        }
        return scopes;
    }
}