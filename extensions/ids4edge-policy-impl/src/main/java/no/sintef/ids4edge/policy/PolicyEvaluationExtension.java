/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package no.sintef.ids4edge.policy;


import no.sintef.ids4edge.context.spi.Ids4EdgeContext;
import org.eclipse.edc.connector.controlplane.catalog.spi.policy.CatalogPolicyContext;
import org.eclipse.edc.connector.controlplane.contract.spi.policy.ContractNegotiationPolicyContext;
import org.eclipse.edc.connector.controlplane.contract.spi.policy.TransferProcessPolicyContext;
//import org.eclipse.edc.connector.policy.monitor.spi.PolicyMonitorContext;
import org.eclipse.edc.connector.policy.monitor.spi.PolicyMonitorContext;
import org.eclipse.edc.policy.engine.spi.AtomicConstraintRuleFunction;
import org.eclipse.edc.policy.engine.spi.PolicyContext;
import org.eclipse.edc.policy.engine.spi.PolicyEngine;
import org.eclipse.edc.policy.engine.spi.RuleBindingRegistry;
import org.eclipse.edc.policy.model.Duty;
import org.eclipse.edc.policy.model.Permission;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;

import static org.eclipse.edc.policy.model.OdrlNamespace.ODRL_SCHEMA;

public class PolicyEvaluationExtension implements ServiceExtension {

    @Inject
    private PolicyEngine policyEngine;

    @Inject
    private RuleBindingRegistry ruleBindingRegistry;

    @Inject
    private Ids4EdgeContext ids4EdgeContext;

    @Override
    public void initialize(ServiceExtensionContext context) {
        //registerActorCredentialEvaluationFunction();
        registerPermissionEvaluationFunction(ActorCredentialEvaluationFunction.ACTOR_CONSTRAIN_KEY, ActorCredentialEvaluationFunction.create(), false);
        registerDutyEvaluationFunction(SupplierTypeEvaluationFunction.SUPPLIER_TYPE_CONSTRAIN_KEY, SupplierTypeEvaluationFunction.create(), true);

    }

    private void registerActorCredentialEvaluationFunction() {
        var actorKey = ActorCredentialEvaluationFunction.ACTOR_CONSTRAIN_KEY;

        bindPermissionFunction(ActorCredentialEvaluationFunction.create(), TransferProcessPolicyContext.class, TransferProcessPolicyContext.TRANSFER_SCOPE, actorKey);
        bindPermissionFunction(ActorCredentialEvaluationFunction.create(), ContractNegotiationPolicyContext.class, ContractNegotiationPolicyContext.NEGOTIATION_SCOPE, actorKey);
        bindPermissionFunction(ActorCredentialEvaluationFunction.create(), CatalogPolicyContext.class, CatalogPolicyContext.CATALOG_SCOPE, actorKey);
        //bindPermissionFunction(ActorCredentialEvaluationFunction.create(), PolicyMonitorContext.class, PolicyMonitorContext.POLICY_MONITOR_SCOPE,  actorKey);
    }


    private <C extends PolicyContext> void registerPermissionEvaluationFunction(String key, AtomicConstraintRuleFunction<Permission, C> function, boolean isMonitor) {
        bindPermissionFunction(function, (Class<C>) TransferProcessPolicyContext.class, TransferProcessPolicyContext.TRANSFER_SCOPE, key);
        bindPermissionFunction(function, (Class<C>) ContractNegotiationPolicyContext.class, ContractNegotiationPolicyContext.NEGOTIATION_SCOPE, key);
        bindPermissionFunction(function, (Class<C>) CatalogPolicyContext.class, CatalogPolicyContext.CATALOG_SCOPE, key);

        if (isMonitor) {
            bindPermissionFunction(function, (Class<C>) PolicyMonitorContext.class, PolicyMonitorContext.POLICY_MONITOR_SCOPE, key);
        }
    }

    private <C extends PolicyContext> void registerDutyEvaluationFunction(String key, AtomicConstraintRuleFunction<Duty, C> function, boolean isMonitor) {
        bindDutyFunction(function, (Class<C>) TransferProcessPolicyContext.class, TransferProcessPolicyContext.TRANSFER_SCOPE, key);
        bindDutyFunction(function, (Class<C>) ContractNegotiationPolicyContext.class, ContractNegotiationPolicyContext.NEGOTIATION_SCOPE, key);
        bindDutyFunction(function, (Class<C>) CatalogPolicyContext.class, CatalogPolicyContext.CATALOG_SCOPE, key);

        if (isMonitor) {
            bindDutyFunction(function, (Class<C>) PolicyMonitorContext.class, PolicyMonitorContext.POLICY_MONITOR_SCOPE, key);
        }
    }

    private <C extends PolicyContext> void bindPermissionFunction(AtomicConstraintRuleFunction<Permission, C> function, Class<C> contextClass, String scope, String constraintType) {
        ruleBindingRegistry.bind("use", scope);
        ruleBindingRegistry.bind(ODRL_SCHEMA + "use", scope);
        ruleBindingRegistry.bind(constraintType, scope);

        policyEngine.registerFunction(contextClass, Permission.class, constraintType, function);
    }

    private <C extends PolicyContext> void bindDutyFunction(AtomicConstraintRuleFunction<Duty, C> function, Class<C> contextClass, String scope, String constraintType) {
        ruleBindingRegistry.bind("use", scope);
        ruleBindingRegistry.bind(ODRL_SCHEMA + "use", scope);
        ruleBindingRegistry.bind(constraintType, scope);

        policyEngine.registerFunction(contextClass, Duty.class, constraintType, function);
    }
}
