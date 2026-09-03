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
            You are a senior platform engineer documenting the technical footprint of
            an existing Mule application ahead of a Java re-engineering effort. Return
            ONLY valid JSON. No markdown, no code fences, no text outside the JSON.

            Required JSON shape:
            {
              "runtime": {
                "muleVersion": "...",
                "javaVersion": "...",
                "buildTool": "..."
              },
              "dependencies": [ { "name": "...", "version": "...", "usedBy": [] } ],
              "connectors": [ { "type": "...", "version": "...", "referencedInFlows": [] } ],
              "configProperties": [],
              "deploymentConstraints": [],
              "unusedDependencies": [],
              "openQuestions": []
            }

            GROUNDING RULES:
            - Populate every field only from values literally present in the input
              JSON (muleRuntime, javaSpecificationVersions, dependencies, connectors
              arrays). Do not assume a build tool, port, or deployment target that
              isn't stated -- use openQuestions instead.
            - "usedBy" / "referencedInFlows": cross-reference the dependency/connector
              against the flows/processors list. If a connector (e.g. SOCKETS) appears
              in dependencies but is never referenced by any flow or processor, put it
              in "unusedDependencies" instead of "connectors", and do not invent a
              purpose for it.
            - "deploymentConstraints" should only list constraints directly implied by
              runtime fields (e.g. minimum Mule version, required Java version). Do not
              invent infrastructure requirements (memory, scaling, cloud provider) not
              present in the input.
            - "configProperties": only include if explicit config/property references
              exist in the input. If none exist, return an empty array.

            Extracted application data:
            %s
            """.formatted(extractedJson);
    }


       /**
     * FLOW DOC
     * Structural diagram of flow -> sub-flow -> processor relationships,
     * rendered as Mermaid flowchart syntax.
     */
    public static String getFlowDocPrompt(String extractedJson) {
        return """
            You are a software architect producing a flow diagram of an existing Mule
            application for a re-engineering effort. Return ONLY valid JSON. No
            markdown, no code fences, no text outside the JSON.

            Required JSON shape:
            {
              "flowSummary": [
                { "flowName": "...", "trigger": "...", "processorSequence": [], "calls": [] }
              ],
              "mermaidFlowchart": "flowchart TD\\n..."
            }

            GROUNDING RULES:
            - "processorSequence" must exactly match the order of the "processors"
              array for that flow in the input. Do not reorder, merge, or omit steps.
            - "calls" must exactly match entries in "flowReferences" / "references" for
              that flow. Do not invent calls to flows that aren't listed.
            - "mermaidFlowchart" must be valid Mermaid flowchart syntax (flowchart TD),
              with one node per processor step and one node per flow. Use flow names
              and processor names as node labels -- do not use generic labels like
              "Step 1" / "Process Data".
            - Represent an HTTP trigger as a distinct starting node labeled with its
              method and path (e.g. "GET /test").
            - Represent a flow-ref / flow call as an edge from the calling processor
              node to the target flow's first node.
            - Do not add error-handling branches, retries, or decision diamonds unless
              the input data shows an actual choice/error-handler processor. A straight
              line flow stays a straight line flow.

            Extracted application data:
            %s
            """.formatted(extractedJson);
    }

     /**
     * SEQUENCE DOC
     * Caller -> Listener -> Flow -> Sub-flow -> Transform -> Response,
     * rendered as Mermaid sequenceDiagram syntax.
     */
    public static String getSequenceDocPrompt(String extractedJson) {
        return """
            You are a software architect producing a sequence diagram of a single
            request/response cycle through an existing Mule application. Return ONLY
            valid JSON. No markdown, no code fences, no text outside the JSON.

            Required JSON shape:
            {
              "participants": [],
              "steps": [
                { "from": "...", "to": "...", "action": "...", "note": "..." }
              ],
              "mermaidSequenceDiagram": "sequenceDiagram\\n..."
            }

            GROUNDING RULES:
            - "participants" must be derived only from actual actors in the input:
              the HTTP caller, the listener/flow, any referenced sub-flow, and the
              transformation step. Do not add participants like "Database" or
              "External API" unless the input's integrations arrays are non-empty.
            - Each entry in "steps" must correspond to a real processor or flow-ref in
              the input, in the order given by the "processors" array. Do not add
              synthetic steps (e.g. "Validate Input", "Log Error") that aren't backed
              by an actual processor of that kind in the data.
            - If the input shows a "transform" processor with DataWeave logic, include
              one step for it and put the actual (paraphrased, not verbatim) effect of
              the expression in "note" -- e.g. note that it builds a JSON message
              incorporating a query parameter, referencing the real variable name.
            - "mermaidSequenceDiagram" must be valid Mermaid sequenceDiagram syntax,
              matching the same actors and steps as above one-to-one. End the diagram
              with the response returned to the caller, using the actual output
              mimeType/schema from typeMetadata if present.
            - If there is only one flow and one sub-flow with no branching, the diagram
              must be a single linear path -- do not add alt/opt blocks that aren't
              justified by real conditional processors in the input.

            Extracted application data:
            %s
            """.formatted(extractedJson);
    }

 





   
}

