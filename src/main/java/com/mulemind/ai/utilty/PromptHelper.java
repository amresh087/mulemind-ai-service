package com.mulemind.ai.utilty;


public class PromptHelper {
    /**
     * FUNCTIONAL DOC
     * Business-facing. No Mule internals, no processor names, no connector
     * versions. Written for a product owner / BA, not an engineer.
     */


public static String getFunctionalDocPrompt(String extractedJson) {

    return """
    You are a Senior Business Analyst documenting the functional behavior of
    a Java Spring Boot microservice migrated from a Mule application.

    Convert the supplied scanner metadata and sequence documentation into
    concise, accurate, business-friendly functional documentation.

    ====================
    SOURCE OF TRUTH
    ====================
    Use ONLY evidence present in the supplied JSON.

    You may use:
    - application scanner metadata
    - API/interface information
    - integration information
    - transformation information
    - sequence/documentation information
    - error information, when explicitly available

    Never invent or assume:
    - business behavior
    - business capability
    - fields
    - validations
    - calculations
    - authentication/authorization
    - integrations
    - retries
    - schedules
    - errors/status codes
    - response structures
    - business rules

    Empty arrays mean there is no evidence. Ignore them.

    ====================
    OUTPUT
    ====================
    Return ONLY one valid JSON object.

    No markdown, code fences, explanations, comments, analysis, or text before
    or after the JSON.

    The first character must be '{' and the last character must be '}'.

    Root object MUST contain exactly these fields:

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

    Do not create any additional root fields.

    ====================
    APPLICATION NAME
    ====================
    Populate applicationName only when the application name is explicitly
    available in the supplied evidence.

    Do not infer a business name from technical names.

    ====================
    PURPOSE
    ====================
    Describe in 1-2 sentences:
    1. What the application receives.
    2. What it does.
    3. What it produces.

    Base this only on explicit evidence.

    Do not infer business meaning from the application name.

    ====================
    BUSINESS CAPABILITY
    ====================
    Describe the business capability provided by the application in concise,
    business-friendly language.

    Only populate this when the capability can be directly established from
    the available evidence.

    Do NOT infer a business capability from:
    - application name alone
    - technical flow names
    - class names
    - connector names
    - dependencies
    - generic technical behavior

    If the evidence only establishes technical behavior and does not establish
    a meaningful business capability, use an empty string.

    Example:
      "Customer information retrieval"

    ====================
    BUSINESS FLOW
    ====================
    Describe the end-to-end business flow in execution order.

    businessFlow MUST be an array of concise complete sentences.

    Each item should represent a meaningful business step supported by the
    evidence.

    Example:
    [
      "The customer submits a request to retrieve customer information.",
      "The application uses the supplied name to construct the required message.",
      "The customer processing operation is executed.",
      "The processed customer information is returned to the caller."
    ]

    Use sequence documentation when available.

    Do not copy technical participants, Mermaid syntax, flow names, processor
    names, implementation expressions, or internal orchestration details.

    Do not invent business steps.

    ====================
    INTERFACES
    ====================
    Create an interface for every actual external entry point.

    Structure:

    {
      "type": "HTTP|KAFKA|MQ|FILE|SCHEDULE|OTHER",
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

    For HTTP APIs:
      External Client -> Application

    Therefore inbound APIs belong in "interfaces".

    Never create an outbound HTTP integration merely because an HTTP API
    exists.

    ====================
    INPUTS
    ====================
    Derive inputs from actual request parameters, payloads, attributes,
    variables, and interface evidence.

    Example:
      attributes.queryParams.name

    means the caller supplies "name" as an HTTP query parameter.

    Structure:

    {
      "name": "",
      "source": "",
      "required": false,
      "description": ""
    }

    Set required=true ONLY when the source explicitly proves the input is
    mandatory.

    Otherwise use false.

    ====================
    PROCESSING
    ====================
    Use scanner processing and sequence documentation to describe the actual
    execution order.

    Convert technical implementation into business language.

    Example:

      variable -> transformation -> subflow -> response

    becomes:

      "The application captures the supplied name, constructs the greeting,
       processes the request, and prepares the response."

    Each processing item must be a complete sentence.

    Do not expose:
    - MuleSoft
    - Mule
    - DataWeave
    - flow references
    - processors
    - connectors
    - implementation expressions
    - internal orchestration

    ====================
    OUTPUTS
    ====================
    Document only output fields supported by:
    - transformation logic
    - response/schema information
    - scanner evidence
    - sequence documentation

    Structure:

    {
      "name": "",
      "destination": "",
      "description": ""
    }

    Do not invent output fields.

    outputExample must be a realistic example derived from actual behavior.

    Example:

      Input name = John
      Output = {"message":"Hello world this is John"}

    Never put implementation expressions in outputExample.

    ====================
    INTEGRATIONS
    ====================
    Create an integration ONLY when explicit evidence exists.

    Structure:

    {
      "type": "KAFKA|MQ|DATABASE|FILE|HTTP|OTHER",
      "name": "",
      "direction": "INPUT|OUTPUT|READ|WRITE|CALL|UNKNOWN",
      "description": "",
      "source": "",
      "destination": "",
      "businessPurpose": ""
    }

    Evidence:

    Kafka:
      integrations.kafka OR kafkaTopics is non-empty.

    MQ:
      integrations.mq OR mqEndpoints is non-empty.

    Database:
      integrations.database OR dbOperations is non-empty.

    File:
      integrations.file OR fileOperations is non-empty.

    HTTP:
      integrations.externalHttp is non-empty.

    Never infer an integration from:
    - application name
    - API name
    - flow name
    - dependency
    - connector
    - variable
    - transformation
    - port
    - listener
    - listener configuration
    - source file

    IMPORTANT:

    Sequence documentation may contain participants such as Database, Kafka,
    MQ, or External HTTP Service.

    Do NOT create an integration merely because such a participant appears
    in the sequence documentation.

    An integration requires explicit scanner evidence as defined above.

    ====================
    DATA TRANSFORMATIONS
    ====================
    For every actual transformation, explain the input-to-output change in
    business language.

    Structure:

    {
      "description": "",
      "input": "",
      "output": "",
      "rules": []
    }

    Example:

      message = "Hello world this is " + name

    becomes:

      "The supplied name is appended to the predefined greeting text."

    Never expose implementation syntax.

    ====================
    BUSINESS RULES
    ====================
    Include ONLY rules supported by actual logic, such as:
    - field mapping
    - field selection
    - concatenation
    - conditions
    - default values
    - calculations
    - filtering
    - routing
    - formatting

    Do not invent business rules.

    Business rules may be included inside the relevant interface or
    dataTransformation object.

    ====================
    ERROR SCENARIOS
    ====================
    Document error scenarios ONLY when explicit evidence exists.

    An error scenario may be derived from:
    - explicit error handling
    - exception handling
    - error responses
    - status codes
    - documented failure paths
    - explicit error sequence steps
    - scanner error metadata

    Structure:

    {
      "scenario": "",
      "condition": "",
      "behavior": "",
      "response": ""
    }

    Each field must contain only evidence-supported information.

    Do not invent:
    - HTTP status codes
    - exception types
    - error messages
    - retry behavior
    - fallback behavior
    - validation failures
    - timeout behavior

    If no explicit error behavior exists, return an empty array.

    ====================
    SEQUENCE DOCUMENTATION
    ====================
    If the "documentation" field contains sequence information, parse and use
    its participants, steps, actions, and notes as evidence of actual behavior.

    The documentation may itself be a JSON string.

    Use sequence information to improve:
    - businessFlow
    - interface descriptions
    - processing descriptions
    - transformation descriptions
    - outputs
    - error scenarios, when explicitly documented

    DO NOT copy the sequence documentation into the output.

    DO NOT create a "sequence" root section.

    Translate technical participants into business behavior.

    Example:

      HTTP Client -> Customer API
      "Submit GET request to retrieve customer information."

    becomes:

      "The customer submits a request to retrieve customer information."

    Do not expose participant names when they are purely technical.

    ====================
    DESCRIPTION QUALITY
    ====================
    Descriptions must be concise, specific, grammatical, and business-friendly.

    Explain, when supported:
    - what is received
    - relevant data
    - how it is processed
    - what changes
    - what is returned

    Avoid vague descriptions such as:

      "Processes request."
      "Creates message."
      "Returns response."

    Prefer:

      "The application uses the name supplied by the caller to construct a
       greeting message and returns the resulting message in the response."

    Do not add detail that is not supported by evidence.

    ====================
    LIMITATIONS
    ====================
    Add a known limitation only when the available evidence is incomplete
    and the missing information is meaningful.

    Examples:

    "The available data does not establish whether the name parameter is mandatory."

    "No input validation rules are evident from the available data."

    "The error response behavior is not defined in the available data."

    "The available evidence does not establish the business purpose of the
     downstream customer processing."

    Do not create limitations for information that is irrelevant.

    ====================
    OPEN QUESTIONS
    ====================
    Add open questions only when important behavior cannot be determined from
    the supplied evidence.

    Examples:

    "Is the name input mandatory for the customer information request?"

    "What response should be returned when customer processing fails?"

    "What business data is returned by the customer processing operation?"

    Do not create questions about integrations when no integration evidence
    exists.

    Do not create questions merely because a technical field is absent.

    ====================
    FORBIDDEN OUTPUT
    ====================
    Do not reproduce scanner metadata fields such as:

    eventType,
    eventVersion,
    documentId,
    documentName,
    tenant,
    objectName,
    status,
    apis,
    application,
    connectors,
    flows,
    flowReferences,
    variables,
    transformations,
    dependencies,
    sourceFiles,
    runtimeInfo,
    typeMetadata,
    kafkaTopics,
    mqEndpoints,
    dbOperations,
    fileOperations,
    scannedAt,
    documentation,
    generatedAt.

    These fields may be used as evidence but must not appear in the output.

    ====================
    IMPORTANT EVIDENCE RULE
    ====================
    Technical participants in sequence documentation are NOT automatically
    integrations.

    For example, if sequence documentation contains:

      Database
      Kafka
      MQ
      External HTTP Service

    but the scanner metadata does not contain explicit integration evidence,
    then:

      integrations = []

    Do not infer integrations from sequence participants alone.

    ====================
    FINAL VALIDATION
    ====================
    Before returning the result verify:

    1. Output is valid parseable JSON.
    2. JSON only; no surrounding text.
    3. Exactly eleven root fields exist.
    4. No scanner metadata is copied.
    5. No sequence documentation is copied.
    6. Empty arrays are ignored.
    7. No integration is inferred.
    8. Inbound APIs are interfaces.
    9. Outbound HTTP requires explicit evidence.
    10. Required flags are evidence-based.
    11. Output fields are evidence-based.
    12. Transformations are converted into business language.
    13. Processing follows the actual sequence.
    14. businessFlow follows the actual execution order.
    15. businessCapability is evidence-based.
    16. errorScenarios contain only explicitly supported error behavior.
    17. Technical implementation syntax is not exposed.
    18. Unsupported assumptions are removed.
    19. Descriptions are complete grammatical sentences.
    20. No custom root fields are created.
    21. Technical sequence participants are not treated as integrations
        without explicit integration evidence.

    ====================
    APPLICATION SCANNER JSON
    ====================
    %s

    Return ONLY the functional documentation JSON.
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
