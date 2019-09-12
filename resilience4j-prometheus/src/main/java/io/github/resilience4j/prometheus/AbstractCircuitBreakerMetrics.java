/*
 * Copyright 2019 Ingyu Hwang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.resilience4j.prometheus;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class AbstractCircuitBreakerMetrics extends Collector {

    protected static final String KIND_FAILED = "failed";
    protected static final String KIND_SUCCESSFUL = "successful";
    protected static final String KIND_IGNORED = "ignored";
    protected static final String KIND_NOT_PERMITTED = "not_permitted";

    protected static final List<String> NAME_AND_STATE = Arrays.asList("name", "state");

    protected final MetricNames names;
    protected final CollectorRegistry collectorRegistry = new CollectorRegistry(true);
    protected final Histogram callsHistogram;

    protected AbstractCircuitBreakerMetrics(MetricNames names) {
        this.names = requireNonNull(names);
        callsHistogram = Histogram.build(names.getCallsMetricName(), "Total number of calls by kind")
                .labelNames("name", "kind")
                .create().register(collectorRegistry);
    }


    /** Defines possible configuration for metric names. */
    public static class MetricNames {

        public static final String DEFAULT_CIRCUIT_BREAKER_CALLS = "resilience4j_circuitbreaker_calls";
        public static final String DEFAULT_CIRCUIT_BREAKER_STATE = "resilience4j_circuitbreaker_state";
        public static final String DEFAULT_CIRCUIT_BREAKER_BUFFERED_CALLS = "resilience4j_circuitbreaker_buffered_calls";
        public static final String DEFAULT_CIRCUIT_BREAKER_FAILURE_RATE = "resilience4j_circuitbreaker_failure_rate";
        public static final String DEFAULT_CIRCUIT_BREAKER_SLOW_CALL_RATE = "resilience4j_circuitbreaker_slow_call_rate";

        /**
         * Returns a builder for creating custom metric names.
         * Note that names have default values, so only desired metrics can be renamed.
         */
        public static Builder custom() {
            return new Builder();
        }

        /** Returns default metric names. */
        public static MetricNames ofDefaults() {
            return new MetricNames();
        }

        private String callsMetricName = DEFAULT_CIRCUIT_BREAKER_CALLS;
        private String stateMetricName = DEFAULT_CIRCUIT_BREAKER_STATE;
        private String bufferedCallsMetricName = DEFAULT_CIRCUIT_BREAKER_BUFFERED_CALLS;
        private String failureRateMetricName = DEFAULT_CIRCUIT_BREAKER_FAILURE_RATE;
        private String slowCallRateMetricName = DEFAULT_CIRCUIT_BREAKER_SLOW_CALL_RATE;

        private MetricNames() {}

        /** Returns the metric name for circuit breaker calls, defaults to {@value DEFAULT_CIRCUIT_BREAKER_CALLS}. */
        public String getCallsMetricName() {
            return callsMetricName;
        }

        /** Returns the metric name for currently buffered calls, defaults to {@value DEFAULT_CIRCUIT_BREAKER_BUFFERED_CALLS}. */
        public String getBufferedCallsMetricName() {
            return bufferedCallsMetricName;
        }

        /** Returns the metric name for failure rate, defaults to {@value DEFAULT_CIRCUIT_BREAKER_FAILURE_RATE}. */
        public String getFailureRateMetricName() {
            return failureRateMetricName;
        }

        /** Returns the metric name for slow call rate, defaults to {@value DEFAULT_CIRCUIT_BREAKER_SLOW_CALL_RATE}. */
        public String getSlowCallRateMetricName() {
            return slowCallRateMetricName;
        }

        /** Returns the metric name for state, defaults to {@value DEFAULT_CIRCUIT_BREAKER_STATE}. */
        public String getStateMetricName() {
            return stateMetricName;
        }

        /** Helps building custom instance of {@link MetricNames}. */
        public static class Builder {
            private final MetricNames metricNames = new MetricNames();

            /** Overrides the default metric name {@value MetricNames#DEFAULT_CIRCUIT_BREAKER_CALLS} with a given one. */
            public Builder callsMetricName(String callsMetricName) {
                metricNames.callsMetricName = requireNonNull(callsMetricName);
                return this;
            }

            /** Overrides the default metric name {@value MetricNames#DEFAULT_CIRCUIT_BREAKER_STATE} with a given one. */
            public Builder stateMetricName(String stateMetricName) {
                metricNames.stateMetricName = requireNonNull(stateMetricName);
                return this;
            }

            /** Overrides the default metric name {@value MetricNames#DEFAULT_CIRCUIT_BREAKER_BUFFERED_CALLS} with a given one. */
            public Builder bufferedCallsMetricName(String bufferedCallsMetricName) {
                metricNames.bufferedCallsMetricName = requireNonNull(bufferedCallsMetricName);
                return this;
            }

            /** Overrides the default metric name {@value MetricNames#DEFAULT_CIRCUIT_BREAKER_FAILURE_RATE} with a given one. */
            public Builder failureRateMetricName(String failureRateMetricName) {
                metricNames.failureRateMetricName = requireNonNull(failureRateMetricName);
                return this;
            }

            /** Overrides the default metric name {@value MetricNames#DEFAULT_CIRCUIT_BREAKER_SLOW_CALL_RATE} with a given one. */
            public Builder slowCallRateMetricName(String slowCallRateMetricName) {
                metricNames.slowCallRateMetricName = requireNonNull(slowCallRateMetricName);
                return this;
            }

            /** Builds {@link MetricNames} instance. */
            public MetricNames build() {
                return metricNames;
            }
        }
    }
}