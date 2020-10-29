import { Router } from '@angular/router';
import { AuthenticationService } from './../auth-services/authentication.service';
import { Component, EventEmitter, Output } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-nav',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  @Output() public sidenavToggle = new EventEmitter();

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  constructor(private breakpointObserver: BreakpointObserver,private authenticationService:AuthenticationService,private router:Router) {}

  logout(){
    this.authenticationService.logOut();
    this.router.navigateByUrl('/login');
  }
}
