grammar queryGrammar;

@header {
package cpen221.mp3.wikimediator.queries;
}


@members {
    // This method makes the lexer or parser stop running if it encounters
    // invalid input and throw a RuntimeException.
    public void reportErrorsAsExceptions() {
        removeErrorListeners();

        addErrorListener(new ExceptionThrowingErrorListener());
    }

    private static class ExceptionThrowingErrorListener
                                              extends BaseErrorListener {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer,
                Object offendingSymbol, int line, int charPositionInLine,
                String msg, RecognitionException e) {
            throw new RuntimeException(msg);
        }
    }
}

fragment P : 'page';
fragment A : 'author';
fragment C : 'category';

GET : 'get';
ITEM :  P | A | C;
WHERE : 'where';
OPERATOR: 'or' | 'and';
IS: 'is';
LPAREN : [(];
RPAREN : [)];
SORTED : 'asc' | 'desc';
STRING : '\'' ( ~'\'' | '\'\'' )* '\'';
WHITESPACE : [ \t\r\n]+ -> skip ;



query : GET ITEM WHERE condition SORTED? EOF;
condition:  LPAREN condition OPERATOR condition RPAREN | simple_condition;
simple_condition: ITEM IS STRING;