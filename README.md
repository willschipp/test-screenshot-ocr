# OCR for Automated Layout Detection

## Scenario

- dynamic content is rendered on a webpage
- due to element styling (e.g. fixed width) text content may not be rendered correctly (hidden, overflowed, etc.)
- automated testing will not necessarily expose such issues and manual/visual checking is required

## Proposal

- create browser screenshots of a final page
- using OCR, extract the text from the screenshots
- compare the text against the expected text
- any words that are compromised due to style or missing will fail to show correctly in OCR

## Components

| Component | Role |
|-----------|------|
| Selenium | Screenshot generator that persists to PNG file |
| Tesseract | OCR library to extract text from images |
| JUnit | Test coordinator |


## Prerequesities

- tesseract 
- node
- maven
- Java IDE
- firefox


## Example

~~1. start the node server on the example directory
`cd src/test/resources/example`
`npm start`~~
1. install npm dependencies
`cd src/test/resources/example`
`npm i`
2. build the sample files
`cd src/test/resources/example`
`npm run build`
3. run the junit test
4. adjust the css file width size
`vi src/test/resources/example/src/App.css`
`inputField { width: 150px; }`
5. rebuild the sample files
`cd src/test/resources/example`
`npm run build`
6. test should fail (doesn't render complete text)


## Note

- samples are English and Korean
- Korean fails due to translation difference from OCR to google translate
- when using STS/Eclipse, you will also need to refresh the project if executing from the app



