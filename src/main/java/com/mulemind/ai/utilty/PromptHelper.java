package com.mulemind.ai.utilty;


public class PromptHelper {

    


    /**
     * FUNCTIONAL DOC
     * Business-facing. No Mule internals, no processor names, no connector
     * versions. Written for a product owner / BA, not an engineer.
     */

public static String getFunctionalDocPrompt(String extractedJson) {


return """
    TASK

    You are a Senior Business Analyst.

    Convert the APPLICATION SCANNER JSON provided at the end of this
    prompt into concise, accurate, business-friendly functional
    documentation.

    The scanner JSON is the ONLY source of truth.

    ============================================================
    ABSOLUTE OUTPUT RULE
    ============================================================

    RETURN ONLY ONE VALID JSON OBJECT.

    DO NOT return the scanner JSON.

    DO NOT copy the scanner JSON.

    DO NOT summarize the scanner JSON.

    DO NOT reproduce scanner fields.

    DO NOT add explanations.

    DO NOT add comments.

    DO NOT add markdown.

    DO NOT use ```json.

    DO NOT use ```.

    DO NOT write "Here is the final JSON".

    DO NOT write "Based on the scanner data".

    DO NOT write any text before or after the JSON.

    The first character MUST be {

    The last character MUST be }

    The output MUST be directly parseable by a JSON parser.

    ============================================================
    ROOT OUTPUT STRUCTURE
    ============================================================

    The output MUST contain ONLY these seven root fields:

    {
      "applicationName": "",
      "purpose": "",
      "interfaces": [],
      "integrations": [],
      "dataTransformations": [],
      "knownLimitations": [],
      "openQuestions": []
    }

    NEVER add another root field.

    NEVER return scanner fields at the root.

    ============================================================
    INTERFACE STRUCTURE
    ============================================================

    For every actual external entry point, create an object inside
    "interfaces".

    Use this structure:

    {
      "type": "HTTP|KAFKA|MQ|FILE|SCHEDULE|OTHER",
      "name": "",
      "method": "",
      "path": "",
      "topic": "",
      "queue": "",
      "description": "",
      "inputs": [
        {
          "name": "",
          "source": "",
          "required": false,
          "description": ""
        }
      ],
      "processing": [],
      "outputs": [
        {
          "name": "",
          "destination": "",
          "description": ""
        }
      ],
      "outputExample": "",
      "businessRules": []
    }

    ============================================================
    INTEGRATION STRUCTURE
    ============================================================

    Use this structure only when an actual external integration
    exists:

    {
      "type": "KAFKA|MQ|DATABASE|FILE|HTTP|OTHER",
      "name": "",
      "direction": "INPUT|OUTPUT|READ|WRITE|CALL|UNKNOWN",
      "description": "",
      "source": "",
      "destination": "",
      "businessPurpose": ""
    }

    ============================================================
    DATA TRANSFORMATION STRUCTURE
    ============================================================

    Use this structure when actual transformation logic exists:

    {
      "description": "",
      "input": "",
      "output": "",
      "rules": []
    }

    ============================================================
    1. SOURCE OF TRUTH
    ============================================================

    Use ONLY evidence found in the scanner JSON.

    Never invent:

    - business processes
    - business capabilities
    - business meanings
    - integrations
    - API behavior
    - fields
    - calculations
    - validation rules
    - authentication
    - authorization
    - retry behavior
    - error handling
    - schedules
    - status codes
    - response structures

    If something cannot be established from the scanner data,
    do not invent it.

    ============================================================
    2. DO NOT COPY SCANNER JSON
    ============================================================

    The scanner JSON is INPUT, not OUTPUT.

    The following scanner fields MUST NEVER appear in the final
    documentation:

    eventType
    eventVersion
    documentId
    documentName
    tenant
    objectName
    status
    apis
    application
    connectors
    flows
    flowReferences
    variables
    transformations
    dependencies
    sourceFiles
    runtimeInfo
    typeMetadata
    kafkaTopics
    mqEndpoints
    dbOperations
    fileOperations
    scannedAt

    IMPORTANT:

    The information from these fields may be used to understand
    application behavior, but the scanner field itself must not be
    copied into the output.

    Example:

    Scanner:

    "variables": [
      {
        "name": "name",
        "expression": "#[attributes.queryParams.name]"
      }
    ]

    Do NOT output:

    "variables": [...]

    Instead use the information to document:

    "name" is an HTTP query parameter.

    ============================================================
    3. EMPTY ARRAY RULE
    ============================================================

    EMPTY ARRAY = NO EVIDENCE.

    Completely ignore empty arrays.

    For example:

    "kafka": []

    means:

    DO NOT create Kafka documentation.

    DO NOT create Kafka integration.

    DO NOT mention Kafka.

    Do not create a negative statement saying Kafka is not used.

    Apply the same rule to:

    kafka
    mq
    database
    file
    externalHttp
    kafkaTopics
    mqEndpoints
    dbOperations
    fileOperations

    ============================================================
    4. INTEGRATION RULE — VERY IMPORTANT
    ============================================================

    NEVER create an integration without direct evidence.

    KAFKA:

    Create Kafka integration ONLY if:

    integrations.kafka contains one or more entries

    OR

    kafkaTopics contains one or more entries.

    If both are empty:

    "integrations": []

    MQ:

    Create MQ integration ONLY if:

    integrations.mq contains one or more entries

    OR

    mqEndpoints contains one or more entries.

    DATABASE:

    Create Database integration ONLY if:

    integrations.database contains one or more entries

    OR

    dbOperations contains one or more entries.

    FILE:

    Create File integration ONLY if:

    integrations.file contains one or more entries

    OR

    fileOperations contains one or more entries.

    OUTBOUND HTTP:

    Create HTTP integration ONLY if:

    integrations.externalHttp contains one or more entries.

    ============================================================
    5. ABSOLUTELY NO INTEGRATION INFERENCE
    ============================================================

    NEVER infer an integration from:

    - application name
    - artifact name
    - API name
    - flow name
    - dependency
    - connector
    - source file
    - runtime version
    - variable name
    - transformation
    - port number
    - listener configuration

    Example:

    If:

    "connectors": [
      {
        "type": "HTTP"
      }
    ]

    this does NOT prove an external HTTP integration.

    Example:

    If:

    "dependencies": {
      "kafka": ["some-kafka-library"]
    }

    this does NOT prove Kafka is used.

    Only actual integration evidence can create an integration.

    ============================================================
    6. APEXHOURS EXAMPLE
    ============================================================

    If the scanner contains:

    "integrations": {
      "kafka": [],
      "mq": [],
      "database": [],
      "file": [],
      "externalHttp": []
    }

    and:

    "kafkaTopics": [],
    "mqEndpoints": [],
    "dbOperations": [],
    "fileOperations": []

    then the output MUST contain:

    "integrations": []

    NEVER generate:

    {
      "type": "KAFKA",
      "name": "kafka"
    }

    NEVER generate:

    "Publishes hourly data to Kafka topic"

    There is no evidence for this statement.

    ============================================================
    7. INBOUND HTTP VS OUTBOUND HTTP
    ============================================================

    The "apis" section represents an INBOUND interface.

    Example:

    "apis": [
      {
        "type": "HTTP",
        "method": "GET",
        "path": "/test"
      }
    ]

    means:

    Client -> Application

    Therefore create an HTTP interface.

    It does NOT mean:

    Application -> External HTTP Service

    Therefore:

    "apis" -> interfaces

    "integrations.externalHttp" -> outbound HTTP integration

    NEVER convert an API into an external HTTP integration.

    The application's:

    - host
    - port
    - listener
    - listener configuration
    - HTTP method
    - API path

    are NOT external integrations.

    ============================================================
    8. APPLICATION NAME
    ============================================================

    Do not infer business meaning from the application name.

    Example:

    applicationName = "apexhours"

    does NOT prove:

    - employee hours
    - working hours
    - time tracking
    - payroll
    - attendance

    Use only actual application behavior.

    ============================================================
    9. PURPOSE
    ============================================================

    "purpose" must explain:

    WHAT the application receives.

    WHAT it does.

    WHAT it produces.

    Use one or two complete sentences.

    Example:

    "The application accepts a name through a query parameter, uses
    it to create a greeting message, and returns the message as JSON."

    ============================================================
    10. INPUTS
    ============================================================

    Use actual request attributes and variables to identify inputs.

    Example:

    "attributes.queryParams.name"

    means:

    name = HTTP query parameter.

    Document:

    "name": "name"

    "source": "HTTP query parameter: name"

    "required": false

    unless the scanner explicitly proves that the parameter is
    mandatory.

    Do NOT assume an input is mandatory.

    Do NOT assume an input is optional unless evidence supports it.

    ============================================================
    11. PROCESSING
    ============================================================

    Use flows and processing information ONLY to understand the
    sequence of business processing.

    Do not expose technical implementation details.

    Example:

    Technical:

    set-variable -> flow-reference -> transformation

    Business description:

    "The application captures the supplied name, creates the
    greeting message, and prepares the response."

    Each processing item must be a complete sentence.

    BAD:

    "Processes request."

    GOOD:

    "The application receives the request at the specified endpoint."

    ============================================================
    12. TRANSFORMATIONS
    ============================================================

    Analyze every populated transformation.

    Transformation logic is important evidence of actual behavior.

    Example:

    "logic": "{ message: \"Hello world this is \" ++ vars.name }"

    means:

    The supplied name is appended to the fixed greeting text and
    placed in the message field.

    Do NOT output:

    "++ vars.name"

    Do NOT output:

    "DataWeave transformation"

    Translate the behavior into business language.

    ============================================================
    13. BUSINESS RULES
    ============================================================

    Derive business rules from actual application logic.

    Valid examples:

    - concatenation
    - conditional logic
    - default values
    - calculations
    - filtering
    - field mapping
    - field selection
    - routing
    - formatting

    Example:

    "message = 'Hello world this is ' + name"

    Business rule:

    "The supplied name is appended to the fixed greeting text."

    Never invent business rules.

    ============================================================
    14. OUTPUT
    ============================================================

    Document only output fields supported by:

    - transformation logic
    - output schema
    - explicit response information

    Example:

    If the scanner contains:

    "schema": {
      "message": "String"
    }

    then document:

    message

    Do NOT invent:

    employeeId
    customerId
    orderId
    hours
    status
    timestamp

    unless scanner evidence supports them.

    ============================================================
    15. OUTPUT EXAMPLE
    ============================================================

    Create outputExample from actual transformation behavior.

    Example:

    Input:

    name = John

    Transformation:

    message = "Hello world this is " + name

    Output:

    {"message":"Hello world this is John"}

    Therefore:

    "outputExample": "{\"message\":\"Hello world this is John\"}"

    Do not put implementation expressions into outputExample.

    ============================================================
    16. DATA TRANSFORMATIONS
    ============================================================

    Add a dataTransformation when actual transformation logic exists.

    Explain:

    - what the input is
    - what the output is
    - how the value changes
    - the actual rule applied

    Do not expose technical expressions.

    ============================================================
    17. KNOWN LIMITATIONS
    ============================================================

    Include only meaningful limitations supported by the scanner.

    Examples:

    "The scanner data does not explicitly establish whether the name
    parameter is mandatory."

    "No input validation rules are evident from the available data."

    "The error response behavior is not defined in the available data."

    Do NOT create limitations just because an integration array is
    empty.

    Do NOT say:

    "Kafka configuration is unavailable."

    ============================================================
    18. OPEN QUESTIONS
    ============================================================

    Add questions only when important behavior cannot be determined.

    Example:

    "Should the name parameter be mandatory?"

    Do NOT ask:

    "What is the Kafka topic?"

    when kafka is empty.

    Do NOT create questions about integrations with no evidence.

    ============================================================
    19. BUSINESS LANGUAGE
    ============================================================

    The final output is for:

    - business stakeholders
    - product owners
    - QA teams
    - support teams
    - API consumers

    Do not mention technical implementation terms such as:

    MuleSoft
    Mule
    DataWeave
    Java
    connector
    processor
    flow
    flow reference
    implementation expression

    Translate technical behavior into understandable business
    language.

    ============================================================
    20. SENTENCE QUALITY
    ============================================================

    Every description MUST be a complete grammatical sentence.

    BAD:

    "Retrieves greeting message from query parameter."

    "Name from query parameter."

    "JSON response."

    "Processes request."

    GOOD:

    "The application accepts a name through the HTTP query parameter."

    "The supplied name is used to create the greeting message."

    "The application returns the generated message as a JSON response."

    ============================================================
    21. AVOID REPETITION
    ============================================================

    Use each section for a different purpose.

    purpose:
    Overall application behavior.

    interface description:
    What the interface does.

    processing:
    Sequence of actual actions.

    businessRules:
    Rules applied to data.

    dataTransformations:
    How input becomes output.

    Do not repeat the same sentence in every section.

    ============================================================
    22. DO NOT CREATE YOUR OWN STRUCTURE
    ============================================================

    NEVER create sections such as:

    "business language": {}

    "input required flags": {}

    "output examples": {}

    "processing": {}

    "integrations": {}

    "scannerData": {}

    "technicalDetails": {}

    "analysis": {}

    Use ONLY the required structure.

    "processing" is an array inside an interface.

    "businessRules" is an array inside an interface.

    "integrations" is an array at the root.

    ============================================================
    23. FINAL VALIDATION
    ============================================================

    Before returning the answer, verify:

    1. Output is JSON only.
    2. Output starts with {.
    3. Output ends with }.
    4. Output has only the seven required root fields.
    5. Scanner JSON is not copied.
    6. Scanner metadata is not returned.
    7. Empty arrays are ignored.
    8. No integration is invented.
    9. Empty Kafka means no Kafka integration.
    10. Empty MQ means no MQ integration.
    11. Empty database means no database integration.
    12. Empty file means no file integration.
    13. Empty externalHttp means no outbound HTTP integration.
    14. Inbound APIs are interfaces.
    15. APIs are never external integrations.
    16. Dependencies do not prove integrations.
    17. Connectors do not prove integrations.
    18. Application name does not prove business meaning.
    19. Transformation logic is used to derive business rules.
    20. Output fields are not invented.
    21. Required flags are not invented.
    22. Technical implementation syntax is not returned.
    23. Every sentence is grammatically complete.
    24. No explanation is returned before or after JSON.

    If any output statement is not supported by scanner evidence,
    REMOVE IT.

    ============================================================
    APPLICATION SCANNER JSON
    ============================================================

    %s

    ============================================================
    END APPLICATION SCANNER JSON
    ============================================================

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
