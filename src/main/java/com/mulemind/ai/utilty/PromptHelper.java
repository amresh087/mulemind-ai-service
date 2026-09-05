package com.mulemind.ai.utilty;


public class PromptHelper {
    /**
     * FUNCTIONAL DOC
     * Business-facing. No Mule internals, no processor names, no connector
     * versions. Written for a product owner / BA, not an engineer.
     */


public static String getFunctionalDocPrompt(String extractedJson) {

    return """
    You are a Senior Business Analyst generating FUNCTIONAL_DOC for a
    Java Spring Boot microservice migrated from a Mule application.

    ====================
    ABSOLUTE OUTPUT RULE
    ====================

    Return ONLY one valid JSON object.

    Do NOT:
    - explain anything
    - repeat this prompt
    - describe documentation rules
    - output markdown
    - output headings
    - output scanner JSON
    - output analysis
    - output comments
    - output text before or after JSON

    First character must be {
    Last character must be }

    The root object MUST contain exactly these TEN fields:

    {
      "applicationName": "",
      "purpose": "",
      "businessCapability": "",
      "businessFlow": [],
      "interfaces": [],
      "integrations": [],
      "dataTransformations": [],
      "errorScenarios": [],
      "knownLimitations": [],
      "openQuestions": []
    }

    Do not add, remove, or rename root fields.

    ====================
    SOURCE OF TRUTH
    ====================

    Use ONLY evidence from the supplied scanner metadata.

    Never invent:
    - business behavior
    - business capability
    - inputs
    - outputs
    - validations
    - calculations
    - integrations
    - retries
    - schedules
    - errors
    - status codes
    - business rules

    Empty arrays mean there is no evidence.

    ====================
    PREVIOUS DOCUMENTATION
    ====================

    If the input contains a "documentation" field containing previously
    generated documentation, IGNORE IT.

    Never copy previous FUNCTIONAL_DOC content.

    ====================
    APPLICATION
    ====================

    applicationName:
    Use only an explicitly provided application name.

    purpose:
    Describe what the application receives, does, and produces.
    Use only supported evidence.

    businessCapability:
    Describe the business capability only when it is directly supported.
    Do not infer it from technical names alone.

    ====================
    BUSINESS FLOW
    ====================

    businessFlow must contain concise complete sentences in actual execution
    order.

    Use only supported processing steps.

    Do not copy technical implementation details.

    ====================
    INTERFACES
    ====================

    Create one interface for each actual external entry point.

    Each interface may contain:

    {
      "type": "",
      "name": "",
      "method": "",
      "path": "",
      "topic": "",
      "queue": "",
      "description": "",
      "inputs": [],
      "processing": [],
      "outputs": [],
      "outputExample": "",
      "businessRules": []
    }

    Populate only fields supported by evidence.

    HTTP inbound APIs are interfaces.

    Do NOT treat an inbound HTTP listener as an outbound HTTP integration.

    ====================
    INPUTS
    ====================

    Derive inputs only from actual request parameters, payloads, attributes,
    variables, schemas, or explicit scanner evidence.

    Input structure:

    {
      "name": "",
      "source": "",
      "required": false,
      "description": ""
    }

    required=true ONLY when mandatory behavior is explicitly proven.

    ====================
    PROCESSING
    ====================

    Processing must be specific to the corresponding interface/flow.

    Do NOT copy processing from another interface.

    Do NOT use generic processing such as:

    "The application processes the request."

    unless that is genuinely all the evidence supports.

    Convert technical implementation into concise business language.

    ====================
    OUTPUTS
    ====================

    Outputs must be derived from the transformation, response, schema, or
    scanner evidence belonging to the SAME interface.

    Output structure:

    {
      "name": "",
      "destination": "",
      "description": ""
    }

    Never assume every API returns "message".

    ====================
    OUTPUT EXAMPLE
    ====================

    outputExample must be valid JSON when an example is possible.

    Example:

    {"message":"Hello John"}

    Property names MUST be quoted.

    Do not invent values or fields.

    If an accurate example cannot be derived, use an empty string.

    ====================
    DATA TRANSFORMATIONS
    ====================

    Create entries only for actual transformations supported by evidence.

    Structure:

    {
      "description": "",
      "input": "",
      "output": "",
      "rules": []
    }

    Translate implementation logic into business language.

    Do not expose implementation expressions.

    ====================
    INTEGRATIONS
    ====================

    Create an integration ONLY when explicit valid scanner evidence exists.

    Valid evidence may include:

    - kafkaTopics
    - mqEndpoints
    - dbOperations
    - fileOperations
    - integrations.database
    - integrations.kafka
    - integrations.mq
    - integrations.file
    - integrations.externalHttp

    Technical participants in sequence documentation are NOT sufficient.

    Do not infer integrations from:

    - dependencies
    - connector names
    - flow names
    - listener configuration
    - API paths
    - application names

    Ignore malformed, empty, ambiguous, or obviously noisy integration values.

    Integration structure:

    {
      "type": "",
      "name": "",
      "direction": "",
      "description": "",
      "source": "",
      "destination": "",
      "businessPurpose": ""
    }

   STRICT INTEGRATION RULE:

    Create an integration ONLY if the actual source value is non-empty,
    meaningful, and clearly proves an integration.

    Empty, missing, malformed, ambiguous, or noisy values MUST NOT create
    an integration.

    Never invent placeholder values such as:
    "kafka-topic", "mq-queue", "db-operation",
    "file-operation", or "external-http".

    If no valid integration evidence exists, return:
    "integrations": []

    Field names alone are NOT evidence.

    ====================
    ERROR SCENARIOS
    ====================

    Create an error scenario ONLY when explicit error evidence exists.

    Valid evidence includes:

    - explicit error handling
    - exception handling
    - error response
    - status code
    - documented failure path

    Do NOT treat a normal interface as an error scenario.

    Structure:

    {
      "scenario": "",
      "condition": "",
      "behavior": "",
      "response": ""
    }

    If no explicit error evidence exists:

    "errorScenarios": []

    ====================
    LIMITATIONS
    ====================

    Add knownLimitations only when missing information materially limits
    functional understanding.

    Do not invent limitations.

    ====================
    OPEN QUESTIONS
    ====================

    Add openQuestions only for important behavior that cannot be determined
    from the supplied evidence.

    Do not create questions merely because technical information is absent.

    ====================
    FLOW ISOLATION
    ====================

    Each interface must use ONLY evidence belonging to that interface/flow.

    Never:
    - copy inputs between APIs
    - copy outputs between APIs
    - copy transformations between APIs
    - copy processing between APIs
    - copy business rules between APIs

    ====================
    FINAL VALIDATION
    ====================

    Before responding verify:

    1. Response is valid JSON.
    2. Response contains ONLY JSON.
    3. Exactly TEN root fields exist.
    4. Root field names exactly match the required structure.
    5. No prompt instructions are returned.
    6. No scanner metadata fields are returned.
    7. Previous documentation is not copied.
    8. No unsupported assumptions are made.
    9. Inbound HTTP APIs are interfaces.
    10. Integrations require explicit valid evidence.
    11. Error scenarios require explicit error evidence.
    12. Each API uses flow-specific processing.
    13. Each API uses flow-specific outputs.
    14. Transformations are evidence-based.
    15. outputExample contains valid JSON when populated.
    16. No technical implementation syntax is exposed.

    ====================
    SCANNER METADATA
    ====================

    %s

    ====================
    FINAL RESPONSE
    ====================

    Return ONLY the FUNCTIONAL_DOC JSON object.
    Do not return these instructions.
    Do not summarize these instructions.
    Do not describe the scanner.
    """.formatted(extractedJson);
}



    /**
     * TECHNICAL DOC
     * Engineer-facing. Runtime, dependencies, config, deployment constraints.
     */
    public static String getTechnicalDocPrompt(String extractedJson) {
      return """
          TASK

          You are a Senior Platform Architect.

          Convert the APPLICATION SCANNER JSON provided at the end of this
          prompt into technical documentation describing the application's
          runtime footprint, dependencies, connector usage, and deployment
          considerations.

          The scanner JSON is the ONLY source of truth.

          ============================================================
          ABSOLUTE OUTPUT RULE
          ============================================================

          RETURN ONLY ONE VALID JSON OBJECT.

          DO NOT return scanner JSON.

          DO NOT copy scanner JSON.

          DO NOT add explanations.

          DO NOT add comments.

          DO NOT add markdown.

          DO NOT add code fences.

          DO NOT write text before or after the JSON.

          The first character MUST be {

          The last character MUST be }

          The output MUST be directly parseable by a JSON parser.

          ============================================================
          REQUIRED OUTPUT STRUCTURE
          ============================================================

          {
            "runtime": {
              "muleVersion": "",
              "javaVersion": "",
              "buildTool": ""
            },
            "dependencies": [
              {
                "name": "",
                "version": "",
                "usedBy": []
              }
            ],
            "connectors": [
              {
                "type": "",
                "version": "",
                "referencedInFlows": []
              }
            ],
            "configProperties": [],
            "deploymentConstraints": [],
            "unusedDependencies": [],
            "openQuestions": []
          }

          NEVER add additional root fields.

          NEVER rename fields.

          NEVER change data types.

          ============================================================
          SOURCE OF TRUTH
          ============================================================

          Use ONLY evidence present in the scanner JSON.

          Never invent:

          - build tools
          - deployment targets
          - cloud providers
          - infrastructure
          - memory requirements
          - CPU requirements
          - scaling strategies
          - networking requirements
          - connector purposes
          - dependency purposes
          - versions
          - property values
          - environment names
          - security requirements

          If a value cannot be established from scanner evidence,
          leave it empty and raise an open question if appropriate.

          ============================================================
          RUNTIME RULES
          ============================================================

          Populate runtime fields only from explicit runtime metadata.

          Valid evidence includes:

          - muleRuntime
          - muleVersion
          - javaVersion
          - javaSpecificationVersions
          - build configuration

          Do not derive versions from dependency names.

          Do not guess versions.

          If build tool is not explicitly identified:

          "buildTool": ""

          and create an appropriate open question.

          ============================================================
          DEPENDENCY RULES
          ============================================================

          Add entries only for actual dependencies explicitly found
          in scanner data.

          Structure:

          {
            "name": "",
            "version": "",
            "usedBy": []
          }

          Use exact names where available.

          Use exact versions where available.

          Do not invent versions.

          ============================================================
          DEPENDENCY USAGE RULES
          ============================================================

          Determine usedBy only from actual scanner evidence.

          Valid evidence:

          - flow processors
          - connector references
          - component references
          - explicit usage mappings

          If usage cannot be proven:

          "usedBy": []

          Never guess usage based on dependency names.

          Example:

          Dependency present:

          mule-sockets-connector

          does NOT prove usage.

          Usage must be evidenced by scanner data.

          ============================================================
          UNUSED DEPENDENCY RULES
          ============================================================

          Add a dependency to unusedDependencies only when:

          1. The dependency exists.
          2. No flow or processor references it.
          3. No scanner evidence proves usage.

          Do not classify a dependency as unused if evidence is unclear.

          Unclear evidence must result in an open question.

          ============================================================
          CONNECTOR RULES
          ============================================================

          Add a connector only when scanner data identifies
          an actual connector.

          Structure:

          {
            "type": "",
            "version": "",
            "referencedInFlows": []
          }

          Connector types may include:

          - HTTP
          - FILE
          - MQ
          - JMS
          - DATABASE
          - SOCKETS
          - KAFKA

          Only include connectors actually present
          in scanner data.

          ============================================================
          CONNECTOR FLOW REFERENCE RULES
          ============================================================

          referencedInFlows must contain only flows
          where scanner evidence proves connector usage.

          Never infer usage.

          Example:

          Connector installed:

          HTTP

          does NOT prove every flow uses HTTP.

          Only actual references may appear.

          ============================================================
          CONFIGURATION PROPERTY RULES
          ============================================================

          Include config properties ONLY when explicit
          property references exist.

          Examples:

          ${host}
          ${port}
          ${database.url}

          Do not invent:

          - database.url
          - kafka.topic
          - mq.queue
          - client.id

          If no property references exist:

          "configProperties": []

          ============================================================
          DEPLOYMENT CONSTRAINT RULES
          ============================================================

          deploymentConstraints must be derived ONLY
          from available runtime evidence.

          Valid examples:

          - "Requires Mule runtime version 4.4."
          - "Requires Java 17."

          Invalid examples:

          - "Requires Kubernetes."
          - "Requires CloudHub."
          - "Requires 4 GB RAM."
          - "Requires autoscaling."

          unless explicitly stated by the scanner.

          ============================================================
          NO INFERENCE RULE
          ============================================================

          NEVER infer technical facts from:

          - application names
          - dependency names
          - file names
          - flow names
          - source folders
          - connector availability
          - version patterns

          Presence does not prove usage.

          Reference does not prove deployment.

          Dependency does not prove runtime behavior.

          ============================================================
          OPEN QUESTION RULES
          ============================================================

          Add questions only when important technical
          information cannot be determined.

          Valid examples:

          - "What build tool is used by the application?"
          - "Can usage of the sockets dependency be confirmed?"
          - "Which deployment platform hosts the application?"

          Do not create questions for fields
          where scanner evidence already exists.

          ============================================================
          EMPTY ARRAY RULE
          ============================================================

          Empty array means no evidence.

          Ignore empty arrays.

          Do not create placeholder entries.

          Example:

          "connectors": []

          means:

          "connectors": []

          not:

          {
            "type": "UNKNOWN"
          }

          ============================================================
          SENTENCE QUALITY RULES
          ============================================================

          Every deployment constraint and open question
          must be a complete grammatical sentence.

          BAD:

          "Java 17"

          GOOD:

          "The application requires Java 17."

          ============================================================
          FINAL VALIDATION
          ============================================================

          Before returning verify:

          1. Output is valid JSON.
          2. Output starts with {.
          3. Output ends with }.
          4. Only required root fields exist.
          5. No scanner JSON is copied.
          6. No field is invented.
          7. No version is invented.
          8. No build tool is invented.
          9. No deployment target is invented.
          10. No connector usage is invented.
          11. No dependency usage is invented.
          12. Empty arrays remain empty.
          13. Configuration properties are evidence-based.
          14. Deployment constraints come only from runtime data.
          15. Open questions address only unresolved facts.
          16. No text exists outside JSON.

          If any statement is not directly supported by
          scanner evidence, REMOVE IT.

          ============================================================
          APPLICATION SCANNER JSON
          ============================================================

          %s

          ============================================================
          END APPLICATION SCANNER JSON
          ============================================================

          Return ONLY the technical documentation JSON.
          """.formatted(extractedJson);
    }

       /**
     * FLOW DOC
     * Structural diagram of flow -> sub-flow -> processor relationships,
     * rendered as Mermaid flowchart syntax.
     */
       public static String getFlowDocPrompt(String extractedJson) {
         return """
             TASK

             You are a Senior Solution Architect.

             Convert the APPLICATION SCANNER JSON provided at the end of this
             prompt into an accurate application flow representation.

             The scanner JSON is the ONLY source of truth.

             ============================================================
             ABSOLUTE OUTPUT RULE
             ============================================================

             RETURN ONLY ONE VALID JSON OBJECT.

             DO NOT return scanner JSON.

             DO NOT copy scanner JSON.

             DO NOT add explanations.

             DO NOT add comments.

             DO NOT add markdown.

             DO NOT add ```json.

             DO NOT add ```.

             DO NOT write any text before or after JSON.

             The first character MUST be {

             The last character MUST be }

             The output MUST be directly parseable JSON.

             ============================================================
             REQUIRED OUTPUT STRUCTURE
             ============================================================

             {
               "flowSummary": [
                 {
                   "flowName": "",
                   "trigger": "",
                   "processorSequence": [],
                   "calls": []
                 }
               ],
               "mermaidFlowchart": ""
             }

             NEVER add additional root fields.

             NEVER rename root fields.

             ============================================================
             FLOW SUMMARY RULES
             ============================================================

             Create one flowSummary object for every actual flow found
             in the scanner data.

             Flow name must come directly from scanner evidence.

             Do not invent flow names.

             Do not merge multiple flows.

             Do not split a flow into multiple entries.

             ============================================================
             TRIGGER RULES
             ============================================================

             Identify trigger only from actual flow metadata.

             Valid examples:

             HTTP GET /customers

             HTTP POST /orders

             Kafka Topic Consumer

             MQ Queue Consumer

             Scheduled Job

             File Listener

             Subflow

             If no trigger is evident and flow is called only by other
             flows, use:

             "Subflow"

             Do not invent endpoints, schedules, queues or topics.

             ============================================================
             PROCESSOR SEQUENCE RULES
             ============================================================

             processorSequence must preserve EXACT execution order.

             The order must match scanner data.

             Do NOT:

             - reorder processors
             - merge processors
             - remove processors
             - summarize processors
             - invent processors

             Every processor must appear once and only once.

             Example:

             Scanner processors:

             [
               "Set Variable",
               "Transform Message",
               "Flow Reference",
               "Logger"
             ]

             Output:

             [
               "Set Variable",
               "Transform Message",
               "Flow Reference",
               "Logger"
             ]

             ============================================================
             FLOW CALL RULES
             ============================================================

             calls must contain ONLY actual flow references
             discovered in scanner data.

             Use flowReferences or references only.

             Never infer calls.

             Never guess calls from naming conventions.

             If no flow references exist:

             "calls": []

             ============================================================
             NO INFERENCE RULE
             ============================================================

             NEVER infer relationships from:

             - file names
             - application name
             - dependencies
             - connector usage
             - processor names
             - variables
             - transformations

             Every flow connection must be explicitly supported by
             scanner evidence.

             ============================================================
             MERMAID FLOWCHART RULES
             ============================================================

             Generate ONLY valid Mermaid flowchart syntax.

             Mermaid must begin with:

             flowchart TD

             Create:

             - one node per flow
             - one node per processor

             Use meaningful labels from scanner evidence.

             Do not use generic labels:

             BAD:

             Step 1
             Step 2
             Process Data

             GOOD:

             Set Variable
             Transform Message
             Publish Customer Event

             ============================================================
             FLOW CONSTRUCTION RULES
             ============================================================

             Connect processors sequentially exactly as execution occurs.

             Example:

             A --> B
             B --> C
             C --> D

             Do not skip steps.

             Do not add shortcuts.

             ============================================================
             HTTP TRIGGER RULES
             ============================================================

             If a flow exposes an HTTP endpoint:

             Create starting node with:

             GET /path

             POST /path

             PUT /path

             DELETE /path

             Example:

             GET_/customer --> setVariable

             HTTP listener must appear as the starting entry node.

             ============================================================
             FLOW REFERENCE RULES
             ============================================================

             When a processor invokes another flow:

             Create edge from calling processor node
             to target flow.

             Example:

             TransformCustomer --> ProcessCustomerFlow

             Only create such edges when an actual
             flow reference exists.

             ============================================================
             DECISION RULES
             ============================================================

             Create branching only when scanner data
             explicitly contains:

             - Choice
             - Router
             - When
             - Otherwise
             - Switch

             If no branching component exists:

             Use straight-line flow.

             Do not invent decisions.

             ============================================================
             ERROR HANDLING RULES
             ============================================================

             Do not generate:

             - error handlers
             - retries
             - exception paths
             - recovery paths

             unless explicitly present in scanner data.

             ============================================================
             EMPTY ARRAY RULE
             ============================================================

             Empty array means no evidence.

             Ignore empty arrays.

             Do not create:

             - fake flow calls
             - fake processors
             - fake triggers

             ============================================================
             VALIDATION RULES
             ============================================================

             Before returning:

             1. Output is valid JSON.
             2. Starts with {.
             3. Ends with }.
             4. Contains only flowSummary and mermaidFlowchart.
             5. Every flow exists in scanner data.
             6. Every processor exists in scanner data.
             7. Processor order is preserved.
             8. Every flow call is supported by scanner evidence.
             9. Mermaid syntax begins with "flowchart TD".
             10. No invented flows.
             11. No invented processors.
             12. No invented integrations.
             13. No invented decision branches.
             14. No invented error handling.
             15. No text outside JSON.

             If anything is not supported by scanner evidence,
             REMOVE IT.

             ============================================================
             APPLICATION SCANNER JSON
             ============================================================

             %s

             ============================================================
             END APPLICATION SCANNER JSON
             ============================================================

             Return ONLY the JSON object.
             """.formatted(extractedJson);
       }


       /**
     * SEQUENCE DOC
     * Caller -> Listener -> Flow -> Sub-flow -> Transform -> Response,
     * rendered as Mermaid sequenceDiagram syntax.
     */
       public static String getSequenceDocPrompt(String extractedJson) {
         return """
             TASK

             You are a Senior Solution Architect.

             Convert the APPLICATION SCANNER JSON provided at the end of this
             prompt into an accurate request/response sequence representation.

             The scanner JSON is the ONLY source of truth.

             ============================================================
             ABSOLUTE OUTPUT RULE
             ============================================================

             RETURN ONLY ONE VALID JSON OBJECT.

             DO NOT return scanner JSON.

             DO NOT copy scanner JSON.

             DO NOT add explanations.

             DO NOT add comments.

             DO NOT add markdown.

             DO NOT add code fences.

             DO NOT write any text before or after the JSON.

             The first character MUST be {

             The last character MUST be }

             The output MUST be directly parseable JSON.

             ============================================================
             REQUIRED OUTPUT STRUCTURE
             ============================================================

             {
               "participants": [],
               "steps": [
                 {
                   "from": "",
                   "to": "",
                   "action": "",
                   "note": ""
                 }
               ],
               "mermaidSequenceDiagram": ""
             }

             NEVER add additional root fields.

             NEVER rename fields.

             ============================================================
             SOURCE OF TRUTH
             ============================================================

             Use ONLY evidence found in the scanner JSON.

             Never invent:

             - participants
             - integrations
             - validation logic
             - authentication logic
             - authorization logic
             - retries
             - exception handling
             - business rules
             - request parameters
             - response fields
             - database calls
             - external API calls
             - Kafka interactions
             - MQ interactions
             - file processing

             unless directly supported by scanner evidence.

             ============================================================
             PARTICIPANT RULES
             ============================================================

             Participants must be derived only from actual actors
             identified in scanner data.

             Allowed examples:

             - HTTP Client
             - API Consumer
             - Main Flow
             - Subflow
             - Referenced Flow
             - Transformation Step
             - Database
             - Kafka
             - MQ
             - External HTTP Service

             IMPORTANT:

             Database, Kafka, MQ, File, or External HTTP participants
             must only be created when actual integration evidence exists.

             Empty integration arrays mean those participants
             MUST NOT be created.

             Do not invent generic participants such as:

             - Backend
             - Service Layer
             - Processor Engine
             - Business Logic
             - Middleware

             unless explicitly represented in scanner data.

             ============================================================
             STEP RULES
             ============================================================

             Each step must correspond to an actual processor,
             flow-ref, listener, transformation, or integration action
             present in scanner data.

             One processor = one sequence step.

             Never merge multiple processors into one step.

             Never skip processors.

             Never reorder processors.

             Execution order must exactly match scanner evidence.

             ============================================================
             FROM/TO RULES
             ============================================================

             from and to must identify actual participants.

             Example:

             HTTP Client -> Customer API

             Customer API -> Transformation

             Transformation -> Customer Subflow

             Customer Subflow -> Customer API

             Customer API -> HTTP Client

             Do not create message exchanges that do not exist.

             ============================================================
             FLOW REFERENCE RULES
             ============================================================

             If a flow references another flow:

             Create a step representing the invocation.

             Example:

             {
               "from": "Main Flow",
               "to": "Customer Subflow",
               "action": "Invoke customer processing."
             }

             Create such steps ONLY when flowReferences
             or references prove the relationship.

             Never infer flow calls from naming conventions.

             ============================================================
             TRANSFORMATION RULES
             ============================================================

             When a transformation exists:

             Create a sequence step for the transformation.

             Describe the business effect.

             Do NOT expose:

             - DataWeave syntax
             - implementation expressions
             - Mule expressions
             - Java expressions

             Example:

             BAD:

             payload.message = "Hello " ++ vars.name

             GOOD:

             Creates a message by appending the supplied name
             to the greeting text.

             ============================================================
             TRANSFORMATION NOTE RULES
             ============================================================

             note must explain the actual business impact
             of the transformation.

             Examples:

             GOOD:

             "The supplied name is combined with a fixed greeting."

             "Customer fields are reformatted into the response structure."

             BAD:

             "Uses DataWeave."

             "Executes transformation."

             "Maps payload."

             ============================================================
             REQUEST RULES
             ============================================================

             If scanner evidence shows an inbound HTTP endpoint:

             First step must represent the client request.

             Example:

             HTTP Client -> Customer API

             Action:

             "Submit GET request to retrieve customer information."

             Use actual method and path if available.

             Do not invent parameters.

             ============================================================
             RESPONSE RULES
             ============================================================

             Final step must return the response
             to the original caller.

             Example:

             Customer API -> HTTP Client

             If response schema or mime type is known,
             describe it.

             If unknown, simply indicate that the response
             is returned.

             Never invent status codes.

             Never invent response formats.

             ============================================================
             INTEGRATION RULES
             ============================================================

             External participants may be created only when
             integration evidence exists.

             Examples:

             integrations.database populated

             integrations.kafka populated

             integrations.mq populated

             integrations.externalHttp populated

             fileOperations populated

             dbOperations populated

             kafkaTopics populated

             mqEndpoints populated

             If evidence is absent:

             Do not create integration participants
             or interaction steps.

             ============================================================
             BRANCHING RULES
             ============================================================

             Create conditional paths only when
             scanner data explicitly contains:

             - Choice
             - Router
             - Switch
             - When
             - Otherwise

             If none exist:

             Use a completely linear sequence.

             ============================================================
             ERROR HANDLING RULES
             ============================================================

             Do not create:

             - exception flows
             - retry flows
             - fallback flows
             - validation failures
             - error responses

             unless explicitly supported by scanner evidence.

             ============================================================
             MERMAID RULES
             ============================================================

             Generate valid Mermaid sequence syntax.

             Mermaid must begin with:

             sequenceDiagram

             Every participant in the participants array
             must appear in Mermaid.

             Every step in steps must appear exactly once
             in Mermaid.

             Mermaid and JSON must represent the same sequence.

             Do not add extra Mermaid messages.

             Do not omit JSON messages.

             ============================================================
             PARTICIPANT CONSISTENCY RULES
             ============================================================

             Every step.from must exist in participants.

             Every step.to must exist in participants.

             No unused participants.

             No missing participants.

             ============================================================
             EMPTY ARRAY RULE
             ============================================================

             Empty array means no evidence.

             Ignore empty arrays.

             Do not create placeholder participants.

             Do not create placeholder interactions.

             ============================================================
             SENTENCE QUALITY RULES
             ============================================================

             Every action must be a complete sentence.

             Every note must be a complete sentence.

             BAD:

             "Transform payload"

             GOOD:

             "The application reformats the incoming data into the response structure."

             ============================================================
             FINAL VALIDATION
             ============================================================

             Before returning verify:

             1. Output is valid JSON.
             2. Starts with {.
             3. Ends with }.
             4. Only required root fields exist.
             5. No scanner JSON is copied.
             6. Every participant is supported by evidence.
             7. Every step maps to a real processor.
             8. Processor order is preserved.
             9. No participant is invented.
             10. No integration is invented.
             11. No business rule is invented.
             12. No validation logic is invented.
             13. No error handling is invented.
             14. Mermaid starts with "sequenceDiagram".
             15. Mermaid exactly matches JSON steps.
             16. Final response returns to original caller.
             17. No text exists outside JSON.

             If any statement is not directly supported by
             scanner evidence, REMOVE IT.

             ============================================================
             APPLICATION SCANNER JSON
             ============================================================

             %s

             ============================================================
             END APPLICATION SCANNER JSON
             ============================================================

             Return ONLY the sequence documentation JSON.
             """.formatted(extractedJson);
       }

     }
