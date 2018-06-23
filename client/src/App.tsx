import * as React from 'react';
import './App.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';

const BootstrapTable = require('react-bootstrap-table-next').default;
import * as ReactBootstrap from 'react-bootstrap';

import logo from './logo.svg';

interface Hotel {
    name: string;
    uri: string;
    price: string;
}

interface AppProps {
}

interface AppState {
    hotels: Array<Hotel>;
    total: number;
    totalPages: number;
    isLoading: boolean;
}

class App extends React.Component<AppProps, AppState> {

    constructor(props: AppProps) {
        super(props);

        this.state = {
            hotels: [],
            total: 0,
            totalPages: 1,
            isLoading: false
        };
        this.loadHotels = this.loadHotels.bind(this);
    }

    loadHotels() {
        const {totalPages} = this.state;
        console.log(totalPages);
        fetch('http://localhost:8080/?from=' + this.state.totalPages +
            '&to=' + this.state.totalPages)
            .then(response => response.json())
            .then(data => this.setState({
                hotels: data.results,
                total: data.totalCount
            }));
    }

    render() {
        const {hotels, total} = this.state;

        function uriFormatter(cell: any, row: any) {
            return (
                <a href={row.uri}><strong>{cell}</strong></a>
            );
        }

        function priceFormatter(cell: any, row: any) {
            return (
                <strong>{cell} €</strong>
            );
        }

        const columns = [{
            text: 'Name',
            dataField: 'name',
            formatter: uriFormatter,
            sort: true
        }, {
            text: 'Price',
            dataField: 'price',
            formatter: priceFormatter,
            sort: true
        }];

        console.log(this.state.totalPages);
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <div>
                    <ReactBootstrap.Button onClick={this.loadHotels}>Search for hotels</ReactBootstrap.Button>
                    <h2>Hotels found, total: {total}</h2>
                    <BootstrapTable keyField="uri" data={hotels} columns={columns}/>
                </div>
            </div>
        );
    }

}

export default App;
